# Análisis y Refactorización - Práctica 3

Este documento detalla el análisis previo, el diagnóstico de problemas de diseño, el registro de pruebas unitarias y la justificación de los cambios realizados durante la refactorización del sistema de gestión de archivos y envío de resultados.

---

## 1. Diagnóstico del Programa Inicial

### Responsabilidades identificadas por método:
1. **`agregarArchivo`**: Se encarga de evaluar el tipo de archivo mediante una cadena de texto (`"pdf"` o `"txt"`) e instanciar el objeto concreto correspondiente para agregarlo a la carpeta.
2. **`obtenerTamanio`**: Se encarga de calcular el tamaño total sumando los archivos de la carpeta y llamando recursivamente a la misma función para las subcarpetas.
3. **`enviarResultado`**: Se encarga de formatear el mensaje con el tamaño obtenido y llamar directamente al método `send_email` del servicio de correo `CorreoLegacy`.

---

### Tres Problemas Concretos Detectados:

#### Problema 1: Alto acoplamiento en la creación de objetos (Violación de OCP)
* **Dónde aparece:** En el método `agregarArchivo`.
* **Qué cambio sería difícil:** Agregar un nuevo tipo de archivo (por ejemplo, `ArchivoImagen` o `ArchivoZip`) requeriría modificar el código existente añadiendo más bloques `if / else if`.
* **Clase/Interfaz responsable:** Patrón **Factory Method** (`CreadorArchivo`, `CreadorPDF`, `CreadorTexto`).

#### Problema 2: Tratamiento no uniforme de elementos (Estructuras duplicadas)
* **Dónde aparece:** En la clase `Carpeta` y en la función `obtenerTamanio`.
* **Qué cambio sería difícil:** La carpeta mantiene dos listas independientes (`archivos` y `subcarpetas`), lo que obliga a procesar por separado los archivos y las carpetas para calcular el tamaño o administrar su contenido.
* **Clase/Interfaz responsable:** Patrón **Composite** (`Elemento`).

#### Problema 3: Dependencia rígida de una API externa (Falta de abstracción en notificaciones)
* **Dónde aparece:** En la llamada a `CorreoLegacy.send_email` dentro de `enviarResultado`.
* **Qué cambio sería difícil:** Cambiar el proveedor de correo o integrar un servicio de notificación diferente (como SMS o WhatsApp) requeriría modificar el método `enviarResultado`, ya que está acoplado al nombre específico de la función `send_email`.
* **Clase/Interfaz responsable:** Patrón **Adapter** (`Notificador` y `AdaptadorCorreo`).

---

## 2. Registro de Pruebas Unitarias

Se ejecutaron los cinco casos de prueba requeridos para comprobar la consistencia de los cálculos antes y después de aplicar las refactorizaciones.

| Caso de Prueba | Entrada de Datos | Resultado Esperado | Resultado Obtenido | Estado |
| :--- | :--- | :---: | :---: | :---: |
| **Prueba 1** | Carpeta vacía (`Carpeta("Vacia")`) | 0 | 0 | **OK** |
| **Prueba 2** | Carpeta con un PDF de 120 | 120 | 120 | **OK** |
| **Prueba 3** | Carpeta con PDF (120) + TXT (80) | 200 | 200 | **OK** |
| **Prueba 4** | Carpeta padre con PDF (120), TXT (80) y subcarpeta con TXT (50) | 250 | 250 | **OK** |
| **Prueba 5** | Carpeta con archivo de tamaño 0 | 0 | 0 | **OK** |

*Nota: La ejecución del ejemplo principal conservó correctamente el valor total de **250**.*

---

## 3. Explicación de Cambios y Distribución de Responsabilidades

### ¿Qué responsabilidad se movió a cada clase?

* **`Elemento` (Interfaz):** Define el contrato uniforme (`obtenerTamanio()`) para que los archivos (hojas) y las carpetas (compuestos) sean tratados de forma homogénea.
* **`Carpeta`:** Asumió la responsabilidad de contener una única lista polimórfica (`List<Elemento>`). Ahora calcula su tamaño delegando recursivamente la llamada `obtenerTamanio()` a sus elementos internos sin necesidad de preguntar si son archivos o carpetas.
* **`Notificador` (Interfaz):** Define el contrato abstracto `enviar(destino, mensaje)` que requiere el cliente del programa.
* **`AdaptadorCorreo` (Adapter):** Se encarga de traducir el método genérico `enviar(destino, mensaje)` hacia el método específico `send_email(to, body)` de la clase existente `CorreoLegacy`.
* **`CreadorArchivo` (Factory Method):** Define la operación para crear un Archivo.
* **`CreadorPDF` (Factory Method):** Se encarga de crear objetos ArchivoPDF.
* **`CreadorTexto` (Factory Method):** Se encarga de crear objetos ArchivoTexto.
---

### ¿Qué permaneció igual para quien usa el programa?

* **Comportamiento externo intacto:** El código interno fue reorganizado mediante `Composite`, `Factory Method` y `Adapter`, pero el resultado observable del programa se conserva: la estructura de archivos produce un tamaño total de **`250`** y el correo simulado se envía al mismo destinatario con el mismo mensaje.
* **Mensaje de correo simulado:** La salida generada por el envío conserva la estructura y el texto exacto:
  ```text
  Para: profesor@universidad.edu
  Tamanio total: 250

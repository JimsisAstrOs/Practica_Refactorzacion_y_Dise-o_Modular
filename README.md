# Práctica 3 - Refactorixación y diseño modular 

# Modelado y Programación
Esta práctica consiste en refactorizar un programa en Java que calcula el tamaño total de una carpeta y sus subcarpetas, manteniendo el mismo resultado observable para quien ejecuta el programa.

El objetivo principal ea mejorar la organización del código y separar responsabilidades mediante los patrones de diseño vistos en la clase:
 
 - Composite
 - Factory Method
 - Adapter

también se realizan pruebas antes y después de la refactorización para comprobat que el comportamiento del programa se mantiene.

# Descripción del problema
El problema representa una carpeta que puede contener archivos PDF, archivos de texto y otras carpetas.

Cada archivo tiene un tamaño y el programa debe calcular el tamaño total de una carpeta, incluyendo los archivos que se encuentran dentro de sus subcarpetas.

El ejemplo principal utilizado durante la práctica es:

```

MyP/
|-- practica.pdf   (120)
|-- notas.txt    (80)
|-- Ejemplos/
   |-- ejemplo.txt (50)`

```

Por lo tanto, el tamaño total esperado es:

120 + 80 + 50 = 250

Además, el programa muestra este resultado y lo envía mediante un correo simulado.

# Objetivos
- Identificar problemas de diseño y responsabilidades mezcladas.
- Separar responsabilidades mediante clases e interfaces.
- Aplica el patrón Composite para trabajar con archivos y carpetas    mediante una interfaz común.
- Aplicar Factory Method para sepaarar la creación de los diferentes tipos de archivos.
- Aplicar Adapter para conectar el programa con el servicio de correo existente.
- Crear y ejecutar pruebas antes y después de la refactorización.
- Comprobar que el resultado final conserva el comportamiento original.

# Patrones utilizados

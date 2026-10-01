import java.util.ArrayList;
import java.util.List;



// ====================
// COMPOSITE
// ====================

interface Elemento {
    int obtenerTamanio();
}

abstract class Archivo implements Elemento {
    String nombre;
    int tamanio;

    Archivo(String nombre, int tamanio) {
        this.nombre = nombre;
        this.tamanio = tamanio;
    }

    @Override
    public int obtenerTamanio() {
        return tamanio;
    }
}

class ArchivoPDF extends Archivo {
    ArchivoPDF(String nombre, int tamanio) {
        super(nombre, tamanio);
    }
}

class ArchivoTexto extends Archivo {
    ArchivoTexto(String nombre, int tamanio) {
        super(nombre, tamanio);
    }
}

abstract class CreadorArchivo {
    abstract Archivo crearArchivo(String nombre, int tamanio);
}

class CreadorPDF extends CreadorArchivo {
    @Override
    Archivo crearArchivo(String nombre, int tamanio){
        return new ArchivoPDF(nombre, tamanio);
    }
}

class CreadorTexto extends CreadorArchivo {
    @Override
    Archivo crearArchivo(String nombre, int tamanio){
        return new ArchivoTexto(nombre, tamanio);
    }
}

class Carpeta implements Elemento {
    String nombre;
    List<Elemento> elementos = new ArrayList<>();

    Carpeta(String nombre) {
        this.nombre = nombre;
    }

    void agregar(Elemento elemento) {
        elementos.add(elemento);
    }

    @Override
    public int obtenerTamanio() {
        int total = 0;

        for (Elemento elemento : elementos) {
            total += elemento.obtenerTamanio();
        }

        return total;
    }
}

// ====================
// CORREO LEGACY
// ====================

class CorreoLegacy {

    void send_email(String to, String body) {
        System.out.println("Para: " + to);
        System.out.println(body);
    }
}


// ====================
// ADAPTER
// ====================

interface Notificador {

    void enviar(String destino, String mensaje);
}

class AdaptadorCorreo implements Notificador {

    private CorreoLegacy correo;

    AdaptadorCorreo(CorreoLegacy correo) {
        this.correo = correo;
    }

    @Override
    public void enviar(String destino, String mensaje) {
        correo.send_email(destino, mensaje);
    }
}


// ====================
// MAIN
// ====================

public class Main {

    static void comprobar(String nombre, int esperado, int obtenido) {

        if (esperado == obtenido) {
            System.out.println("OK: " + nombre + " | esperado=" + esperado + " | obtenido=" + obtenido);
        } else {
            System.out.println("FALLO: " + nombre + " | esperado=" + esperado + " | obtenido=" + obtenido);
        }
    }


    static void ejecutarPruebas() {

        // Prueba 1: carpeta vacia
        Carpeta vacia = new Carpeta("Vacia");

        comprobar(
            "Carpeta vacia",
            0,
            vacia.obtenerTamanio()
        );


        // Prueba 2: PDF de 120
        Carpeta soloPDF = new Carpeta("Solo PDF");

        soloPDF.agregar(
            new ArchivoPDF("archivo.pdf", 120)
        );

        comprobar(
            "PDF de 120",
            120,
            soloPDF.obtenerTamanio()
        );


        // Prueba 3: PDF de 120 + TXT de 80
        Carpeta dosArchivos = new Carpeta("Dos archivos");

        dosArchivos.agregar(
            new ArchivoPDF("archivo.pdf", 120)
        );

        dosArchivos.agregar(
            new ArchivoTexto("notas.txt", 80)
        );

        comprobar(
            "PDF + TXT",
            200,
            dosArchivos.obtenerTamanio()
        );


        // Prueba 4: carpeta con subcarpeta
        Carpeta padre = new Carpeta("Padre");
        padre.agregar(new ArchivoPDF("practica.pdf", 120));
        padre.agregar(new ArchivoTexto("notas.txt", 80));
        Carpeta hija = new Carpeta("Hija");

        hija.agregar(
            new ArchivoTexto("ejemplo.txt", 50)
        );

        padre.agregar(hija);

        comprobar("Ejemplo completo", 250, padre.obtenerTamanio());


        // Prueba 5: archivo de tamaño 0
        Carpeta cero = new Carpeta("Cero");

        cero.agregar(new ArchivoTexto("vacio.txt", 0));

        comprobar("Archivo de tamaño 0", 0, cero.obtenerTamanio());

        //Pruebas Factory Method
        CreadorArchivo creadorPDF = new CreadorPDF();
        Archivo pdf = creadorPDF.crearArchivo("prueba.pdf", 100);

        if (pdf instanceof ArchivoPDF) {
            System.out.println("OK: Factory crea ArchivoPDF");
        } else {
            System.out.println("FALLO: Factory no crea ArchivoPDF");
        }
        
        CreadorArchivo creadorTexto = new CreadorTexto();
        Archivo texto = creadorTexto.crearArchivo("prueba.txt", 100);

        if (texto instanceof ArchivoTexto) {
            System.out.println("OK: Factory crea ArchivoTexto");
        } else {
            System.out.println("FALLO: Factory no crea ArchivoTexto");
        }
    }


    // ====================
    // ENVIO DE RESULTADO
    // ====================

    static void enviarResultado(
        Carpeta carpeta,
        String destino,
        Notificador notificador
    ) {

        int tamanio = carpeta.obtenerTamanio();

        String mensaje = "Tamanio total: " + tamanio;

        notificador.enviar(destino, mensaje);
    }


    public static void main(String[] args) {

        // ====================
        // EJEMPLO
        // ====================

        Carpeta clase = new Carpeta("MyP");

        CreadorArchivo creadorPDF = new CreadorPDF();
        CreadorArchivo creadorTexto = new CreadorTexto();

        clase.agregar(creadorPDF.crearArchivo("practica.pdf", 120));
        clase.agregar(creadorTexto.crearArchivo("notas.txt", 80));

        Carpeta ejemplos = new Carpeta("Ejemplos");
        ejemplos.agregar(creadorTexto.crearArchivo("ejemplo.txt", 50));

        clase.agregar(ejemplos);


        // Debe imprimir 250
        System.out.println(clase.obtenerTamanio());


        // ====================
        // PRUEBAS
        // ====================

        ejecutarPruebas();


        // ====================
        // ADAPTER
        // ====================

        CorreoLegacy correo = new CorreoLegacy();

        Notificador notificador = new AdaptadorCorreo(correo);

        enviarResultado(clase, "profesor@universidad.edu", notificador);
    }
}

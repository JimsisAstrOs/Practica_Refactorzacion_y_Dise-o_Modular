import java.util.ArrayList;
import java.util.List;

// CODIGO INICIAL DE LA PRACTICA 3.

//COMPOSITE

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
    public int obtenerTamanio(){
        int total = 0;

        for (Elemento elemento : elementos) {
            total += elemento.obtenerTamanio();
        }
        return total;
    }

 }

//CORREO LEGACY

class CorreoLegacy {
    void send_email(String to, String body) {
        System.out.println("Para: " + to);
        System.out.println(body);
    }
}

//MAIN

public class Main {
    static void comprobar( String nombre, int esperado, int obtenido) {
        if (esperado == obtenido) {
            System.out.println("OK: " + nombre);
        }
        else {
            System.out.println(" FALLO: " + nombre
                                        + " | esperado=" + esperado
                                        + " | obtenido=" + obtenido);
        }
    }

    static void ejecutarPruebas() {
        //Prueba 1, carpeta vacia
        Carpeta vacia = new Carpeta("Vacia");

        comprobar( "Carpeta vacia", 0, vacia.obtenerTamanio());

        //Prueba 2, PDF de 120
        Carpeta soloPDF = new Carpeta("Solo PDF");

        soloPDF.agregar( new ArchivoPDF("archivo.pdf", 120));

        comprobar("PDF de 120", 120, soloPDF.obtenerTamanio());

        //Prueba 3, PDF de 120 + txt de 80
        Carpeta dosArchivos = new Carpeta("Dos archivos");

        dosArchivos.agregar( new ArchivoPDF("archivo.pdf", 120));
        dosArchivos.agregar(new ArchivoTexto("notas.txt",80));

        comprobar("PDF + TXT", 200, dosArchivos.obtenerTamanio());

        //Prueba 4, carpeta con subcarpeta
        Carpeta padre = new Carpeta("Padre");
        Carpeta hija = new Carpeta("Hija");

        hija.agregar(
            new ArchivoTexto("ejemplo.txt", 50)
        );
        padre.agregar(hija);

        comprobar("Carpeta con subcarpeta", 50, padre.obtenerTamanio());

        //Prueba 4, archivo de tamaño 0
        Carpeta cero = new Carpeta("Cero");
        cero.agregar(new ArchivoTexto("vacio.txt", 0));

        comprobar("Archivo de tamaño 0", 0, cero.obtenerTamanio());
    }

    public static void main(String[] args) {
        //ejemplo 
        Carpeta clase = new Carpeta("MyP");
        clase.agregar(new ArchivoPDF("practica.pdf", 120));
        clase.agregar(new ArchivoTexto("notas.txt", 80));

        Carpeta ejemplos = new Carpeta("Ejemplos");
        ejemplos.agregar(new ArchivoTexto("ejemplo.txt", 50));

        clase.agregar(ejemplos);
        //aqui se suponeee nos tendria que imprimir 250
        System.out.println(clase.obtenerTamanio());

        //PRUEBAS
        ejecutarPruebas();
        //CORRERO peroo todavia no hacemos adapter

        CorreoLegacy correo = new CorreoLegacy();
        correo.send_email(
            "profesor@universidad.edu",
             "Tamanio total: " + clase.obtenerTamanio()
            );

    }
}
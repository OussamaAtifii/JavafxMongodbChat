package org.iesalandalus.programacion.chat.modelo.negocio.utilidades;

import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;

import java.io.*;

public class Ficheros {
    public static void guardarUsuario(String ruta, Usuario usuario) {
        try {
            FileOutputStream fichero = new FileOutputStream(ruta);
            ObjectOutputStream ficheroSalida = new ObjectOutputStream(fichero);

            ficheroSalida.writeObject(usuario);
            ficheroSalida.close();
            System.out.println("Escritura terminada");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Usuario leerUsuario(String ruta) {
        Usuario usuario = null;
        try {
            FileInputStream fichero = new FileInputStream(ruta);
            ObjectInputStream ficheroEntrada = new ObjectInputStream(fichero);

            usuario = (Usuario) ficheroEntrada.readObject();
            ficheroEntrada.close();
            System.out.println("Lectura terminada");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return usuario;
    }

    public static void vaciarFichero(String ruta) {
        try {
            File fichero = new File(ruta);
            fichero.delete();
            fichero.createNewFile();
            System.out.println("Vaciado terminado");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

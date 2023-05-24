package org.iesalandalus.programacion.chat.modelo.negocio.mongodb;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Usuarios {

    private static final String COLECCION = "usuarios";

    private final MongoCollection<Document> coleccionUsuarios;
    private static Usuarios instancia;

    public Usuarios() {
        coleccionUsuarios = MongoDB.getBD().getCollection(COLECCION);
    }

    public static Usuarios getInstancia() {
        if (instancia == null)
            instancia = new Usuarios();

        return instancia;
    }

    public void insertar(Usuario usuario) {
        if (usuario == null)
            throw new NullPointerException("ERROR: No se puede insertar un usuario nulo.");

        if (buscar(usuario) == null) {
            coleccionUsuarios.insertOne(MongoDB.getDocumento(usuario));
        }
    }

    public Usuario buscar(Usuario usuario) {
        Document documentoUsuario =
                coleccionUsuarios.find().filter(
                        and(
                                eq(MongoDB.USER_NAME, usuario.getUserName()),
                                eq(MongoDB.PASSWORD, usuario.getPassword()))).first();
        return MongoDB.getUsuario(documentoUsuario);
    }

    public Usuario buscarUsername(Usuario usuario) {
        Document documentoUsuario =
                coleccionUsuarios.find().filter(
                        eq(MongoDB.USER_NAME, usuario.getUserName())).first();
        return MongoDB.getUsuario(documentoUsuario);
    }

    public List<Usuario> get() {
        List<Usuario> usuarios = new ArrayList<>();

        FindIterable<Document> coleccionUsuariosOrdenada =
                coleccionUsuarios.find().sort(Sorts.ascending(MongoDB.USER_NAME));

        for (Document documentoVehiculo : coleccionUsuariosOrdenada) {
            Usuario usuario = MongoDB.getUsuario(documentoVehiculo);
            usuarios.add(usuario);
        }
        return usuarios;
    }
}

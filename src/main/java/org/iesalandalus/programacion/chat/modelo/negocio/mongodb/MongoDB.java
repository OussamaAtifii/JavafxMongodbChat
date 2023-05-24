package org.iesalandalus.programacion.chat.modelo.negocio.mongodb;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.iesalandalus.programacion.chat.modelo.dominio.Mensaje;
import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;

public class MongoDB {

    public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    // private static final String SERVIDOR="localhost";
    private static final String SERVIDOR = "damprog.e5dxiep.mongodb.net";
    private static final int PUERTO = 27017;
    private static final String BD = "chat";
    private static final String USUARIO = "admin";
    private static final String CONTRASENA = "1234";

    public static final String USER_NAME = "Username";
    public static final String PASSWORD = "Password";

    public static final String CONTENIDO_MENSAJE = "Mensaje";
    public static final String USER_NAME_DESTINO = "Destino";
    public static final String USER_NAME_ORIGEN = "Origen";
    public static final String HORA = "Hora";

    private static MongoClient conexion = null;

    private MongoDB() {
        // Evitamos que se cree el constructor por defecto
    }

    public static MongoDatabase getBD() {
        if (conexion == null) {
            establecerConexion();
        }

        return conexion.getDatabase(BD);
    }

    private static void establecerConexion() {

        String connectionString;
        ServerApi serverApi;
        MongoClientSettings settings;

        if (!SERVIDOR.equals("localhost")) {
            connectionString = "mongodb+srv://" + USUARIO + ":" + CONTRASENA + "@" + SERVIDOR
                    + "/?retryWrites=true&w=majority";
            serverApi = ServerApi.builder()
                    .version(ServerApiVersion.V1)
                    .build();

            settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .serverApi(serverApi)
                    .build();
        } else {
            connectionString = "mongodb://" + USUARIO + ":" + CONTRASENA + "@" + SERVIDOR + ":" + PUERTO;
            MongoCredential credenciales = MongoCredential.createScramSha1Credential(USUARIO, BD,
                    CONTRASENA.toCharArray());

            settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(connectionString))
                    .credential(credenciales)
                    .build();
        }

        // Creamos la conexión con el serveridos según el setting anterior
        conexion = MongoClients.create(settings);

        try {
            if (!SERVIDOR.equals("localhost")) {
                MongoDatabase database = conexion.getDatabase(BD);
                database.runCommand(new Document("ping", 1));
            }
        } catch (MongoException e) {
            e.printStackTrace();

        }

        System.out.println("Conexión a MongoDB realizada correctamente.");

    }

    public static void cerrarConexion() {
        if (conexion != null) {
            conexion.close();
            conexion = null;
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }

    public static Document getDocumento(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        String username = usuario.getUserName();
        String password = usuario.getPassword();
        return new Document().append(USER_NAME, username).append(PASSWORD, password);
    }

    public static Usuario getUsuario(Document documentoUsuario) {
        if (documentoUsuario == null) {
            return null;
        }
        return new Usuario(documentoUsuario.getString(USER_NAME), documentoUsuario.getString(PASSWORD));
    }

    public static Document getDocumento(Mensaje mensaje) {
        if (mensaje == null) {
            return null;
        }
        String mensajeADocumento = mensaje.getMensaje();
        String origen = mensaje.getOrigen().getUserName();
        String destino = mensaje.getDestino().getUserName();
        String hora = mensaje.getHora().format(FORMATO_HORA);
        return new Document().append(CONTENIDO_MENSAJE, mensajeADocumento).append(USER_NAME_ORIGEN, origen).
                append(USER_NAME_DESTINO, destino).append(HORA, hora);
    }

    public static Mensaje getMensaje(Document documentoMensaje) {

        if (documentoMensaje == null) {
            return null;
        }
        return new Mensaje(documentoMensaje.getString(CONTENIDO_MENSAJE),
                Usuario.obtenerUsuarioUsername(documentoMensaje.getString(USER_NAME_ORIGEN)),
                Usuario.obtenerUsuarioUsername(documentoMensaje.getString(USER_NAME_DESTINO)),
                LocalTime.parse(documentoMensaje.getString(HORA), FORMATO_HORA));
    }
}

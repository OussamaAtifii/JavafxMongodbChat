package org.iesalandalus.programacion.chat.modelo.negocio.mongodb;

import com.mongodb.client.FindIterable;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.iesalandalus.programacion.chat.modelo.dominio.Mensaje;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Mensajes {
	private static final String COLECCION = "mensajes";

	private MongoCollection<Document> coleccionMensajes;
	private static Mensajes instancia;

	public Mensajes() {
		coleccionMensajes = MongoDB.getBD().getCollection(COLECCION);
	}

	public static Mensajes getInstancia() {
		if (instancia == null)
			instancia = new Mensajes();

		return instancia;
	}

	public List<Mensaje> get() {
		List<Mensaje> mensajes = new ArrayList<>();

		FindIterable<Document> coleccionMensajesOrdenada = coleccionMensajes.find().sort(Sorts.ascending(MongoDB.HORA));

		for (Document documentoCliente : coleccionMensajesOrdenada) {
			Mensaje mensaje = MongoDB.getMensaje(documentoCliente);
			mensajes.add(mensaje);
		}
		return mensajes;
	}

	public void insertar(Mensaje mensaje) {
		if (mensaje == null)
			throw new NullPointerException("ERROR: No se puede insertar un mensaje nulo.");

		if (buscar(mensaje) == null) {
			coleccionMensajes.insertOne(MongoDB.getDocumento(mensaje));
		}
	}

	public Mensaje buscar(Mensaje mensaje) {
		Document documentoMensaje = coleccionMensajes.find()
				.filter(and(eq(MongoDB.CONTENIDO_MENSAJE, mensaje.getMensaje()),
						eq(MongoDB.USER_NAME_ORIGEN, mensaje.getOrigen().getUserName()),
						eq(MongoDB.USER_NAME_DESTINO, mensaje.getDestino().getUserName())))
				.first();
		return MongoDB.getMensaje(documentoMensaje);
	}
}

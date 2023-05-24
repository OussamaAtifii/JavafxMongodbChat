package org.iesalandalus.programacion.chat.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.iesalandalus.programacion.chat.modelo.dominio.Mensaje;
import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;
import org.iesalandalus.programacion.chat.modelo.negocio.mongodb.Mensajes;
import org.iesalandalus.programacion.chat.modelo.negocio.mongodb.Usuarios;
import org.iesalandalus.programacion.chat.modelo.negocio.utilidades.Ficheros;
import org.iesalandalus.programacion.chat.utilidades.Dialogos;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class ControladorChat {

    @FXML
    private Button btnDesconectar;

    @FXML
    private Button btnEnviar;

    @FXML
    private Label lblChatUsuario;

    @FXML
    private ListView<Usuario> lvUsuarios;

    @FXML
    private TableColumn<Mensaje, String> tcCuando;

    @FXML
    private TableColumn<Mensaje, String> tcMensaje;

    @FXML
    private TableColumn<Mensaje, String> tcOrigen;

    @FXML
    private TextField tfMensajeAEnviar;

    @FXML
    private TableView<Mensaje> tvChat;

    @FXML
    private TableView<Usuario> tvUsuarios;

    @FXML
    private TableColumn<Usuario, String> tcUsuario;

    private final String RUTA_FICHERO_USUARIO = "../ChatOussamaAtifi/src/datos/usuario.data";
    public static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final ObservableList<Mensaje> obsMensajes = FXCollections.observableArrayList();
    private final ObservableList<Usuario> obsUsuarios = FXCollections.observableArrayList();

    private Usuario userOrigen;

    @FXML
    private void initialize() {
    }

    public void inicializaDatosObservables(Usuario usuario) {
        userOrigen = usuario;
        obsMensajes.addAll(Mensajes.getInstancia().get());
        obsUsuarios.addAll(Usuarios.getInstancia().get());

        obsUsuarios.remove(userOrigen);
        tcUsuario.setCellValueFactory(user -> new SimpleStringProperty(user.getValue().getUserName()));
        tvUsuarios.setItems(obsUsuarios);
    }

    private void mostrarUsuarios() {
        obsUsuarios.remove(userOrigen);
        tcUsuario.setCellValueFactory(usuario -> new SimpleStringProperty(usuario.getValue().getUserName()));
        tvUsuarios.setItems(obsUsuarios);
    }

    private void mostrarMensajesUsuario(Usuario usuarioSeleccionado) {
        ObservableList<Mensaje> obsMensajesMostrar = FXCollections.observableArrayList();

        for (Mensaje mensaje : obsMensajes) {
            if (mensaje.getOrigen().equals(userOrigen) && mensaje.getDestino().equals(usuarioSeleccionado)) {
                obsMensajesMostrar.add(mensaje);
            }
            if (mensaje.getOrigen().equals(usuarioSeleccionado) && mensaje.getDestino().equals(userOrigen)) {
                obsMensajesMostrar.add(mensaje);
            }
        }

        tcMensaje.setCellValueFactory(mensaje -> new SimpleStringProperty(">  " + mensaje.getValue().getMensaje()));
        tcCuando.setCellValueFactory(mensaje -> new SimpleStringProperty(mensaje.getValue().getHora().format(FORMATO_HORA)));
        tcOrigen.setCellValueFactory(mensaje -> new SimpleStringProperty(mensaje.getValue().getOrigen().toString()));
        tvChat.setItems(obsMensajesMostrar);
    }

    @FXML
    private void usuarioSeleccionado(MouseEvent event) {
        Usuario usuario = tvUsuarios.getSelectionModel().getSelectedItem();
        lblChatUsuario.setVisible(true);

        if (usuario != null) {
            lblChatUsuario.setText(userOrigen + " - " + usuario);
            mostrarMensajesUsuario(usuario);
        }

        tfMensajeAEnviar.setText("");
    }

    @FXML
    private void desconectar(ActionEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Cancelar", "¿Seguro quieres cancelar la operación?", null)) {
            Ficheros.vaciarFichero(RUTA_FICHERO_USUARIO);
            ((Stage) btnDesconectar.getScene().getWindow()).close();
        } else {
            event.consume();
        }
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String textoMensaje = tfMensajeAEnviar.getText();
        Usuario usuarioDestino = tvUsuarios.getSelectionModel().getSelectedItem();

        if (usuarioDestino == null && textoMensaje.isBlank()) {
            Dialogos.mostrarDialogoAdvertencia("Destinatario", "No se puede mandar un mensaje sin destinatario");
        }

        if (!textoMensaje.isBlank()) {
            Mensaje mensaje = new Mensaje(textoMensaje, userOrigen, usuarioDestino, LocalTime.now());
            Mensajes.getInstancia().insertar(mensaje);
            obsMensajes.add(mensaje);
            obsMensajes.sort(Comparator.comparing(Mensaje::getHora));
            mostrarMensajesUsuario(usuarioDestino);
            tfMensajeAEnviar.setText("");
        } else {
            Dialogos.mostrarDialogoAdvertencia("Mensaje Vacio", "No se puede mandar un mensaje vacio");
        }
    }

}

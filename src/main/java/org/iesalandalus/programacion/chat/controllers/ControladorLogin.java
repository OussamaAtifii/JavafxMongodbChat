package org.iesalandalus.programacion.chat.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;
import org.iesalandalus.programacion.chat.modelo.negocio.mongodb.Usuarios;
import org.iesalandalus.programacion.chat.modelo.negocio.utilidades.Ficheros;
import org.iesalandalus.programacion.chat.utilidades.Dialogos;

public class ControladorLogin {
    private final String RUTA_FICHERO_USUARIO = "../ChatOussamaAtifi/src/datos/usuario.data";

    @FXML
    private Button btnConectar;

    @FXML
    private Label lblError;

    @FXML
    private CheckBox chkRecordarme;

    @FXML
    private TextField tfUsuario;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private void initialize() {
        Usuario usuario = Ficheros.leerUsuario(RUTA_FICHERO_USUARIO);
        if (usuario != null) {
            tfUsuario.setText(usuario.getUserName());
            tfPassword.setText(usuario.getPassword());
            chkRecordarme.setSelected(true);
        }
    }

    @FXML
    void conectar(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        if (username.isBlank() || password.isBlank()) {
            lblError.setText("");
            lblError.setVisible(true);
            lblError.setStyle("-fx-text-fill: red");
            lblError.setText("Usuario y/o contraseña vacios");
        } else {
            cargarVentanaChat(new Usuario(username, password));
        }
    }

    @FXML
    void iniciarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../recursos/vistas/Signin.fxml"));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            Stage escenarioPrincipal = new Stage();
            escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
            escenarioPrincipal.setTitle("Login");
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setResizable(false);
            escenarioPrincipal.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarVentanaChat(Usuario usuarioEncontrado) {
        Usuario usuario = Usuarios.getInstancia().buscar(usuarioEncontrado);

        if (chkRecordarme.isSelected() && usuario != null) {
            Ficheros.guardarUsuario(RUTA_FICHERO_USUARIO, usuario);
        }
        if (!chkRecordarme.isSelected() && usuario != null) {
            Ficheros.vaciarFichero(RUTA_FICHERO_USUARIO);
        }

        if (usuario != null) {
            ((Stage) btnConectar.getScene().getWindow()).close();
            lblError.setVisible(false);
            try {
                FXMLLoader loader = new FXMLLoader();
                ControladorChat controlador = new ControladorChat();
                loader.setController(controlador);
                loader.setLocation(getClass().getResource("../recursos/vistas/Chat.fxml"));
                Parent raiz = loader.load();
                controlador = loader.getController();
                controlador.inicializaDatosObservables(usuario);
                Scene escena = new Scene(raiz);
                Stage escenario = new Stage();
                escenario.setOnCloseRequest(e -> confirmarSalida(escenario, e));
                escenario.initModality(Modality.APPLICATION_MODAL);
                escenario.setScene(escena);
                escenario.setResizable(false);
                escenario.setTitle("Chat");
                escenario.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lblError.setText("");
            lblError.setVisible(true);
            lblError.setStyle("-fx-text-fill: red");
            lblError.setText("Usuario y/o Contraseña incorrectos");
        }
    }

    private void confirmarSalida(Stage escenarioPrincipal, WindowEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estas seguro de que quieres salir de la aplicación?",
                escenarioPrincipal)) {
            escenarioPrincipal.close();
        } else
            event.consume();
    }
}

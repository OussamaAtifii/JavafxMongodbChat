package org.iesalandalus.programacion.chat.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.iesalandalus.programacion.chat.modelo.dominio.Usuario;
import org.iesalandalus.programacion.chat.modelo.negocio.mongodb.Usuarios;

import javafx.event.ActionEvent;
import org.iesalandalus.programacion.chat.utilidades.Dialogos;

public class ControladorSignin {

    @FXML
    private Button btnSignin;

    @FXML
    private Button btnSignup;

    @FXML
    private Label lblError;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private PasswordField tfRepeatPassword;

    @FXML
    private TextField tfUsuario;

    @FXML
    void probando(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        String repeatPassword = tfRepeatPassword.getText();

        if (username.isBlank() || password.isBlank() || repeatPassword.isBlank()) {
            setLblError("Rellena todos los campos");
        } else if (!password.equals(repeatPassword)) {
            setLblError("Las contraseñas tienen que ser iguales");
        } else {
            Usuario usuario = new Usuario(username, password);
            if (Usuarios.getInstancia().buscarUsername(usuario) == null) {
                Usuarios.getInstancia().insertar(usuario);
                Dialogos.mostrarDialogoConfirmacion("Crear usuario", "Usuario creado correctamente", null);
            } else {
                setLblError("Nombre de usuario no disponible");
            }
        }
    }

    private void setLblError(String error) {
        lblError.setText("");
        lblError.setVisible(true);
        lblError.setStyle("-fx-text-fill: red");
        lblError.setText(error);
    }
}

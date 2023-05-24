package org.iesalandalus.programacion.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.iesalandalus.programacion.chat.modelo.negocio.mongodb.MongoDB;
import org.iesalandalus.programacion.chat.utilidades.Dialogos;

public class Main extends Application {

    @Override
    public void start(Stage escenarioPrincipal) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("recursos/vistas/Login.fxml"));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz);
            escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
            escenarioPrincipal.setTitle("Login");
            escenarioPrincipal.setScene(escena);
            escenarioPrincipal.setResizable(false);
            escenarioPrincipal.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void confirmarSalida(Stage escenarioPrincipal, WindowEvent event) {
        if (Dialogos.mostrarDialogoConfirmacion("Salir", "¿Estas seguro de que quieres salir de la aplicación?",
                escenarioPrincipal)) {
            MongoDB.cerrarConexion();
            escenarioPrincipal.close();
        } else
            event.consume();
    }
}

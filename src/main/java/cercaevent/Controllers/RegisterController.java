package cercaevent.Controllers;

import java.io.IOException;

import cercaevent.model.Usuari;
import cercaevent.serveis.App;
import cercaevent.serveis.serveiUsuari;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField name;
    @FXML
    private TextField surname;
    @FXML
    private TextField nickname;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField msg;

    // Tasca 5: Controlar el rol del nou usuari
    @FXML
    private void registrar() throws IOException {
        try {
            // Determino el rol que assignarem al nou usuari.
            // Per defecte creem un USER. Només si l'usuari que ha obert
            // la finestra és un ADMIN, assignarem el rol ADMIN.
            String rolAssignat = "USER";
            Usuari usuariActual = serveiUsuari.getUsuariLoguejat();

            if (usuariActual != null && "ADMIN".equals(usuariActual.getRol())) {
                rolAssignat = "ADMIN";
            }

            // Delego la inserció a serveiUsuari per centralitzar la lògica.
            serveiUsuari.registrarNuevoUsuario(
                    nickname.getText(),
                    name.getText(),
                    surname.getText(),
                    email.getText(),
                    password.getText(),
                    rolAssignat);

            // Si qui crea l'usuari és un admin (estem dins de l'aplicació), tornem a la
            // vista principal.
            // Si venim del Login (usuari normal que es registra), tornem al Login.
            if (usuariActual != null) {
                App.setRoot("CercaEvent");
            } else {
                App.setRoot("Login");
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg.setText("Error: " + e.getMessage());
            msg.setVisible(true);
        }
    }

    // Tornar segons si hi ha sessió activa
    @FXML
    private void Salir() {
        try {
            if (serveiUsuari.getUsuariLoguejat() != null) {
                App.setRoot("CercaEvent");
            } else {
                App.setRoot("Login");
            }
        } catch (Exception e) {
            System.out.println("Error al sortir:");
            e.printStackTrace();
        }
    }
}
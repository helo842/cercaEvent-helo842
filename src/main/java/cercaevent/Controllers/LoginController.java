/*
 * Controlador de login: gestiona l'autenticació d'usuaris i la validació
 * bàsica de les credencials mostrant alertes en cas d'error.
 */
package cercaevent.Controllers;

import java.io.IOException;

import cercaevent.serveis.App;
import cercaevent.serveis.serveiUsuari;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

@FXML
    private TextField usuari;

    @FXML
    private TextField contraseña;

    @FXML
    private TextField msg;

    @FXML
    private void validarUsuari() throws IOException {
        try {
            serveiUsuari.existeixUsuari(usuari.getText(), contraseña.getText());
            App.setRoot("CercaEvent");
        } catch (Exception e) {
            msg.setText("Error: " + e.getMessage());
            msg.setText(e.getMessage());
            msg.setVisible(true);
            e.printStackTrace();
        }
    }
    @FXML
    private void irRegistroPag() throws IOException {
        try {
            App.setRoot("Register");
        } catch (Exception e) {
            System.out.println("Error al cargar la página de registro:");
            e.printStackTrace();
        }
}
    }






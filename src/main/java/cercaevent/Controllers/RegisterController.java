package cercaevent.Controllers;

import java.io.IOException;

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


@FXML
    private void registrar() throws IOException {
        try {
            
            serveiUsuari.registrarNuevoUsuario(nickname.getText(), name.getText(), surname.getText(), email.getText(), password.getText());
            App.setRoot("Login");
        } catch (Exception e) {
            e.printStackTrace(); 
            msg.setText("Error: " + e.getMessage());
            msg.setVisible(true);
        }
    }
 @FXML
    private void Salir() {
        try {
            App.setRoot("Login");
        } catch (Exception e) {
            System.out.println("Error al salir de la aplicación:");
            e.printStackTrace();
        }

}}


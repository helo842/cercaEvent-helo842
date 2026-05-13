package cercaevent.serveis;

import java.io.IOException;

import org.h2.tools.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Inicio l'aplicació mostrant la pantalla de Login.
        // He triat una mida per defecte (640x480) que és suficient per les pantalles
        // FXML.
        // Si cal, l'usuari/jo podem ajustar-ho més endavant segons disseny.

        scene = new Scene(loadFXML("Login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        // Carrego el recurs FXML des del directori resources.
        // Nota: l'App utilitza el nom de fitxer sense ruta, per exemple "Login" ->
        // "Login.fxml".
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        try {

            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("");
            System.out.println("Servidor H2 engegat a: " + webServer.getURL());
            System.out.println("Consola H2 activa a: " + webServer.getURL());
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");

        } catch (Exception e) {
            System.out.println("");
            System.out.println("ERROR!!!!");
            System.out.println("Exception: " + e.getMessage());
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            e.printStackTrace();
        }

        launch();
    }

}
/*
 * Classe App: utilitats per canviar arrels FXML i iniciar l'aplicació JavaFX.
 * Aquest fitxer s'encarrega de la càrrega de recursos i del canvi de pantalles.
 */
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
        
        scene = new Scene(loadFXML("Login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
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
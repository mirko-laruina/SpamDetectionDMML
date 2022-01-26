package Application;
import javafx.application.Application;

public class Start {
    public static void main(String[] args) {
        // Loading the UI this way allows the dynamic loading of JavaFX libraries
        // no need to run the app with VM options to include the libraries
        Application.launch(UIApplication.class, args);
    }
}

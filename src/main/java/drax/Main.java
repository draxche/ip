package drax;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** Starts the FXML-based JavaFX interface for Drax. */
public class Main extends Application {
    private final Drax drax = new Drax();

    /**
     * Loads the main window, injects Drax into its controller, and displays it.
     *
     * @param stage primary JavaFX stage
     * @throws IOException if the main-window FXML resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainLayout = fxmlLoader.load();
        MainWindow mainWindow = fxmlLoader.getController();
        mainWindow.setDrax(drax);

        stage.setTitle("Drax");
        stage.setResizable(true);
        stage.setMinHeight(600);
        stage.setMinWidth(400.0);
        stage.setScene(new Scene(mainLayout));
        stage.show();
    }
}

/*
    !!! To be removed:
    !!! TO BE DONE:
     1) Change the CSS styling in whatever way you want and make it specific to each command
     2) Change the profile pictures and make them circular
     5) Sound effects when sending messages
     6) Change title and icon of app

 */
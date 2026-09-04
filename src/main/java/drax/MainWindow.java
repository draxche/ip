package drax;

import java.util.Timer;
import java.util.TimerTask;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Controls the main JavaFX window defined in {@code MainWindow.fxml}. */
public class MainWindow extends AnchorPane {
    private final Image userImage = new Image(getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image draxImage = new Image(getClass().getResourceAsStream("/images/DaDrax.png"));

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Drax drax;

    /** Connects behavior that depends on controls injected from the FXML view. */
    @FXML
    private void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the shared Drax instance and displays its startup greeting.
     *
     * @param drax Drax instance that owns the application's state and logic
     */
    public void setDrax(Drax drax) {
        this.drax = drax;
        dialogContainer.getChildren().add(DialogBox.getDraxDialog(drax.greet(), draxImage));
    }

    /** Passes user input to Drax and adds both sides of the conversation to the view. */
    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String draxText = drax.getResponse(userText);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getDraxDialog(draxText, draxImage)
        );
        userInput.clear();
        if (userText.equals("bye")) {
            scheduleExit();
        }
    }

    /** Preserves the short delay that allows the farewell dialog to appear before exit. */
    private void scheduleExit() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 500);
    }
}

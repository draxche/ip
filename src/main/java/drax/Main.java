package drax;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Sets up and displays the JavaFX user interface for Drax. */
public class Main extends Application {
    private final Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private final Image draxImage = new Image(this.getClass().getResourceAsStream("/images/DaDrax.png"));
    private final Drax drax = new Drax();

    private ScrollPane scrollPane;
    private VBox dialogContainer;
    private TextField userInput;
    private Button sendButton;
    private Scene scene;

    @Override
    public void start(Stage stage) {
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        DialogBox dialogBox = new DialogBox(drax.greet(), draxImage);
        dialogContainer.getChildren().add(dialogBox);

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setTitle("Drax");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);
        scrollPane.setPrefSize(385.0, 535.0);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);
        sendButton.setPrefWidth(55.0);
        userInput.setOnAction(event -> handleUserInput());
        sendButton.setOnAction(event -> handleUserInput());

        AnchorPane.setTopAnchor(scrollPane, 1.0);
        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);
        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Passes the current input to Drax and displays both sides of the conversation.
     */
    private void handleUserInput() {
        String userText = userInput.getText();
        String draxText = drax.getResponse(userText);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getDraxDialog(draxText, draxImage)
        );
        userInput.clear();
        if (userText.equals("bye")) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0);
                }
            }, 1000);
        }
    }
}

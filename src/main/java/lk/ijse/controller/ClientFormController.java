package lk.ijse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.Client.Client;

import java.io.*;
import java.nio.file.Files;



public class ClientFormController {
    @FXML
    private AnchorPane emojiPane;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane txtArea;

    @FXML
    private Label txtDisplayName;

    @FXML
    private TextField txtMsgField;

    @FXML
    private VBox vBox;
    private String sender;
    private String displayName;
    private Client client;

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                HBox hBox = new HBox();
                hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; -fx-alignment: center-right;");

                // Display the image in an ImageView or any other UI component
                ImageView imageView = new ImageView(new Image(new FileInputStream(selectedFile)));
                imageView.setStyle("-fx-padding: 10px;");
                imageView.setFitHeight(180);
                imageView.setFitWidth(100);

                hBox.getChildren().addAll(imageView);
                vBox.getChildren().add(hBox);

                client.sendImage(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        try {
            String text = txtMsgField.getText();
            if (text != null) {
                appendText(text);
                client.sendMessage(text);
                txtMsgField.clear();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "message is empty").show();
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong : server down").show();
        }
    }

    private void appendText(String text) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-right;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(text);
        messageLbl.setStyle("-fx-background-color:  #7f8c8d;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        vBox.getChildren().add(hBox);
    }


    @FXML
    void msgEnterOnAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnSendOnAction(null);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void writeMessage(String message) {
        HBox hBox = new HBox();
        hBox.setStyle("-fx-alignment: center-left;-fx-fill-height: true;-fx-min-height: 50;-fx-pref-width: 520;-fx-max-width: 520;-fx-padding: 10");
        Label messageLbl = new Label(message);
        messageLbl.setStyle("-fx-background-color:   #3742fa;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center-left;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");
        hBox.getChildren().add(messageLbl);
        Platform.runLater(() -> {

            vBox.getChildren().add(hBox);
        });

    }

    public void setImage(byte[] bytes, String sender) {
        HBox hBox = new HBox();
        Label messageLbl = new Label(sender);
        messageLbl.setStyle("-fx-background-color:   #2980b9;-fx-background-radius:15;-fx-font-size: 18;-fx-font-weight: normal;-fx-text-fill: white;-fx-wrap-text: true;-fx-alignment: center;-fx-content-display: left;-fx-padding: 10;-fx-max-width: 350;");

        hBox.setStyle("-fx-fill-height: true; -fx-min-height: 50; -fx-pref-width: 520; -fx-max-width: 520; -fx-padding: 10; " + (sender.equals(client.getName()) ? "-fx-alignment: center-right;" : "-fx-alignment: center-left;"));
        Platform.runLater(() -> {
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(bytes)));
            imageView.setStyle("-fx-padding: 10px;");
            imageView.setFitHeight(180);
            imageView.setFitWidth(100);

            hBox.getChildren().addAll(messageLbl, imageView);
            vBox.getChildren().add(hBox);

        });
    }
    @FXML
    void cryingFaceOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE22");
        emojiPane.setVisible(false);
    }

    @FXML
    void faceWithTearsOfJoyOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE02");
        emojiPane.setVisible(false);
    }

    @FXML
    void griningFaceEmojiOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE00");
        emojiPane.setVisible(false);
    }

    @FXML
    void griningFaceWithSweatOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE05");
        emojiPane.setVisible(false);
    }

    @FXML
    void griningSquintingOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE06");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHeartEyesOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE0D");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithHornsOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE08");
        emojiPane.setVisible(false);
    }

    @FXML
    void smilingFaceWithOpenHandsOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83E\uDD17");
        emojiPane.setVisible(false);
    }

    @FXML
    void sunglassFaceOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDE0E");
        emojiPane.setVisible(false);
    }

    @FXML
    void thumbsUpOnAction(MouseEvent event) {
        txtMsgField.appendText("\uD83D\uDC4D");
        emojiPane.setVisible(false);
    }


    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        txtDisplayName.setText(displayName);
    }
}

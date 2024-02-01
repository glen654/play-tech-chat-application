package lk.ijse.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import lk.ijse.dto.UserDto;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.awt.SystemColor.text;

public class ClientFormController {
    @FXML
    private Label txtDisplayName;

    @FXML
    private AnchorPane emojiPane;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane txtArea;

    @FXML
    private TextField txtMsgField;

    @FXML
    private VBox vBox;
    private String displayName;
    private File file;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter writer;
    private String finalName;
    @FXML
    private ImageView imageView;
    private Pane imagePane;
    private ImageView clickedImage;


    public void initialize(){
        //setImageView();
        new Thread(() ->{
            try {
                socket = new Socket("localhost",3001);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(),true);
                writer.println("joi"+displayName+"~joining");

                while (true){
                    //reading response
                    String receive = bufferedReader.readLine();
                    String[] split = receive.split("~");
                    String name = split[0];
                    String message = split[1];

                    //find which type of message is came
                    String firstChars = "";
                    if (name.length() > 3) {
                        firstChars = name.substring(0, 3);
                    }
                    if (firstChars.equalsIgnoreCase("img")){
                        String[] imgs = name.split("img");
                        finalName = imgs[1];
                    }else if(firstChars.equalsIgnoreCase("joi")){
                        String[] imgs = name.split("joi");
                        finalName = imgs[1];
                    }else if(firstChars.equalsIgnoreCase("lef")){
                        String[] imgs = name.split("lef");
                        finalName = imgs[1];
                    }
                    if (firstChars.equalsIgnoreCase("img")){
                        if (finalName.equalsIgnoreCase(displayName)){

                            //adding image to message
                            File receiveFile = new File(message);
                            Image image = new Image(receiveFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(170);
                            imageView.setFitWidth(200);
                            imageView.setOnMouseClicked(mouseEvent -> {
                                clickedImage.setImage(imageView.getImage());
                                imagePane.setVisible(true);
                            });
                            //adding sender to message
                            Text text = new Text("~ Me");
                            text.getStyleClass().add("send-text");

                            //add time
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            Text time = new Text(dtf.format(now));
                            time.getStyleClass().add("time-text");
                            HBox timeBox = new HBox();
                            timeBox.getChildren().add(time);
                            timeBox.setAlignment(Pos.BASELINE_RIGHT);

                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView, timeBox);

                            HBox hBox = new HBox(10);
                            hBox.getStyleClass().add("send-box");
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);

                            //adding message to message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setAlignment(Pos.CENTER_LEFT);
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }else {
                            //adding image to message
                            File receives = new File(message);
                            Image image = new Image(receives.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(170);
                            imageView.setFitWidth(200);
                            imageView.setOnMouseClicked(mouseEvent -> {
                                clickedImage.setImage(imageView.getImage());
                                imagePane.setVisible(true);
                            });

                            //adding sender to message
                            Text text = new Text("~ "+finalName);
                            text.getStyleClass().add("receive-text");

                            //add time
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            Text time = new Text(dtf.format(now));
                            time.getStyleClass().add("time-text");
                            HBox timeBox = new HBox();
                            timeBox.getChildren().add(time);
                            timeBox.setAlignment(Pos.BASELINE_RIGHT);

                            VBox vbox = new VBox(10);
                            vbox.getChildren().addAll(text, imageView, timeBox);

                            HBox hBox = new HBox(10);
                            hBox.getStyleClass().add("receive-box");
                            hBox.setMaxHeight(190);
                            hBox.setMaxWidth(220);
                            hBox.getChildren().add(vbox);

                            //adding message to message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }
                    }else if(firstChars.equalsIgnoreCase("joi")) {
                        if (finalName.equalsIgnoreCase(displayName)){

                            //adding name of client which join the chat
                            Label text = new Label("You have join the chat");
                            text.getStyleClass().add("join-text");
                            HBox hBox = new HBox();
                            hBox.getChildren().add(text);
                            hBox.setAlignment(Pos.CENTER);

                            Platform.runLater(() -> {
                                vBox.getChildren().add(hBox);

                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }else{
                            Label text = new Label(finalName+" has join the chat");
                            text.getStyleClass().add("join-text");
                            HBox hBox = new HBox();
                            hBox.getChildren().add(text);
                            hBox.setAlignment(Pos.CENTER);

                            Platform.runLater(() -> {
                                vBox.getChildren().add(hBox);

                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }
                    }else if(firstChars.equalsIgnoreCase("lef")){
                        //adding name of client which left the chat
                        Label text = new Label(finalName+" has left the chat");
                        text.getStyleClass().add("left-text");
                        HBox hBox = new HBox();
                        hBox.getChildren().add(text);
                        hBox.setAlignment(Pos.CENTER);

                        Platform.runLater(() -> {
                            vBox.getChildren().add(hBox);

                            HBox hBox1 = new HBox();
                            hBox1.setPadding(new Insets(5, 5, 5, 10));
                            vBox.getChildren().add(hBox1);
                        });
                    } else{
                        if(name.equalsIgnoreCase(displayName)){

                            //add message
                            TextFlow tempFlow = new TextFlow();
                            Text text = new Text(message);
                            text.setStyle("-fx-fill: white");
                            text.setWrappingWidth(200);
                            tempFlow.getChildren().add(text);
                            tempFlow.setMaxWidth(200);

                            //add sender name
                            Text nameText = new Text("~ Me");
                            nameText.getStyleClass().add("send-text");

                            //add time
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            Text time = new Text(dtf.format(now));
                            time.getStyleClass().add("time-text");
                            HBox timeBox = new HBox();
                            timeBox.getChildren().add(time);
                            timeBox.setAlignment(Pos.BASELINE_RIGHT);
                            VBox vbox = new VBox(10);
                            vbox.setPrefWidth(210);
                            vbox.getChildren().addAll(nameText, tempFlow, timeBox);

                            //add all into message
                            HBox hBox = new HBox(12);
                            hBox.getStyleClass().add("send-box");
                            hBox.setMaxWidth(220);
                            hBox.setMaxHeight(50);
                            hBox.getChildren().add(vbox);
                            StackPane stackPane = new StackPane(hBox);
                            stackPane.setAlignment(Pos.CENTER_RIGHT);

                            //add message into message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(stackPane);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }else {
                            //add message
                            TextFlow tempFlow = new TextFlow();
                            Text text = new Text(message);
                            text.setStyle("-fx-fill: white");
                            text.setWrappingWidth(200);
                            tempFlow.getChildren().add(text);
                            tempFlow.setMaxWidth(200);

                            //add sender name
                            Text nameText = new Text("~ "+name);
                            nameText.getStyleClass().add("receive-text");

                            //add time
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
                            LocalDateTime now = LocalDateTime.now();
                            Text time = new Text(dtf.format(now));
                            time.getStyleClass().add("time-text");
                            HBox timeBox = new HBox();
                            timeBox.getChildren().add(time);
                            timeBox.setAlignment(Pos.BASELINE_RIGHT);
                            VBox vbox = new VBox(10);
                            vbox.setPrefWidth(210);
                            vbox.getChildren().addAll(nameText, tempFlow, timeBox);

                            //add all into message
                            HBox hBox = new HBox();
                            hBox.getStyleClass().add("receive-box");
                            hBox.setMaxWidth(220);
                            hBox.setMaxHeight(50);
                            hBox.getChildren().add(vbox);

                            //add message into message area
                            Platform.runLater(() -> {
                                vBox.getChildren().addAll(hBox);
                                scrollPane.layout();
                                scrollPane.setVvalue(2.0);

                                //adding space between messages
                                HBox hBox1 = new HBox();
                                hBox1.setPadding(new Insets(5, 5, 5, 10));
                                vBox.getChildren().add(hBox1);
                            });
                        }
                    }
                    file = null;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
        emojiPane.setVisible(!emojiPane.isVisible());
    }

    @FXML
    void btnImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select the image");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        file = fileChooser.showOpenDialog(txtMsgField.getScene().getWindow());
        if (file != null){
            txtMsgField.setText("1 image selected");
            txtMsgField.setEditable(false);
        }
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        if (!txtMsgField.getText().isEmpty()) {
            sendTextMessage(txtMsgField.getText());
            txtMsgField.setEditable(true);
            txtMsgField.clear();
        }
    }

    private void sendTextMessage(String text) {
        writer.println("text" + txtDisplayName.getText() + "~" + text);
    }

    @FXML
    void msgEnterOnAction(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            btnSendOnAction(null);
        }
    }

    private void setImageView() {
        double cornerRadius = 20.0; // Set the desired corner radius
        Rectangle clip = new Rectangle(imageView.getFitWidth(), imageView.getFitHeight());
        clip.setArcWidth(cornerRadius);
        clip.setArcHeight(cornerRadius);
        imageView.setClip(clip);
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

    public void initUser(UserDto userDto) {
        this.displayName = userDto.getDisplayName();
        txtDisplayName.setText(displayName);
    }

}

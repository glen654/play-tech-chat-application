package lk.ijse.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.dto.UserDto;


import java.io.*;
import java.net.Socket;
import java.nio.file.Files;


public class UserController {
    @FXML
    private VBox messageContainer;
    @FXML
    private AnchorPane rootNode;

    @FXML
    private Label txtDisplayName;

    @FXML
    private TextField txtMsgField;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String displayName;
    private boolean isImage;


    public void initialize(){
        new Thread(() -> {
            System.out.println("User controller initialized");
            try {
                socket = new Socket("localhost",3001);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                System.out.println("Socket and streams initialized");
                receiveMessages();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private void receiveMessages() {
       /* try {
            while(true) {
                System.out.println("Waiting for messages");
                String message = dataInputStream.readUTF();
                System.out.println("Received messages: " + message);
                Platform.runLater(() -> {
                    appendMessage(message,isSenderMessage(message));
                });
            }
        } catch (IOException e) {
                throw new RuntimeException(e);
        }*/
        try {
            while (true) {
                System.out.println("Before readUTF");
                String receivedMessage = dataInputStream.readUTF();
                System.out.println("After readUTF");
                System.out.println("Before Platform.runLater");
                Platform.runLater(() -> {
                    System.out.println("Adding message to UI: " + receivedMessage);
                    handleMessage(receivedMessage);
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*Task<Void> receiveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    while (true) {
                        System.out.println("Before readUTF");
                        String receivedMessage = dataInputStream.readUTF();
                        System.out.println("After readUTF");
                        System.out.println("Before Platform.runLater");
                        Platform.runLater(() -> {
                            System.out.println("Adding message to UI: " + receivedMessage);
                            handleMessage(receivedMessage);
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(receiveTask).start();*/
    }

    private void handleMessage(String receivedMessage) {
       /* System.out.println("Before UI Update");
        System.out.println("Current Thread: " + Thread.currentThread().getName());

        Platform.runLater(() ->{
            System.out.println("Inside Platform.runLater()");
            System.out.println("Current Thread (inside runLater): " + Thread.currentThread().getName());
            if (receivedMessage.startsWith("IMAGE_MARKER")) {
                receiveImageFromServer();
            } else {
                boolean isSenderMessage = isSenderMessage(receivedMessage);
                System.out.println("Adding message to UI: " + receivedMessage);
                appendMessage(receivedMessage, isSenderMessage);
                messageContainer.layout();
            }
        });
        System.out.println("After UI Update");*/
        System.out.println("Before UI Update");
        System.out.println("Current Thread: " + Thread.currentThread().getName());

        if (Platform.isFxApplicationThread()) {
            System.out.println("On FX Application Thread");
            processMessage(receivedMessage);
        } else {
            Platform.runLater(() -> {
                System.out.println("Inside Platform.runLater()");
                System.out.println("Current Thread (inside runLater): " + Thread.currentThread().getName());
                processMessage(receivedMessage);
            });
        }

        System.out.println("After UI Update");
    }

    private void processMessage(String receivedMessage) {
        if (receivedMessage.startsWith("IMAGE_MARKER")) {
            receiveImageFromServer();
        } else {
            boolean isSenderMessage = isSenderMessage(receivedMessage);
            System.out.println("Adding message to UI: " + receivedMessage);
            appendMessage(receivedMessage, isSenderMessage);
            messageContainer.layout();
        }
    }

    private void receiveImageFromServer() {
        try {
            int imageLength = dataInputStream.readInt();
            byte[] imageBytes = new byte[imageLength];
            dataInputStream.readFully(imageBytes);
            Platform.runLater(() -> {
                appendImage(imageBytes, isSenderMessage(""));
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendImage(byte[] imageBytes, boolean senderMessage) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            ImageView imageView = new ImageView(image);
            Label messageLabel = new Label();
            messageLabel.setGraphic(imageView);
            if (isSenderMessage("")) {
                messageLabel.getStyleClass().add("sender-message");
            } else {
                messageLabel.getStyleClass().add("other-message");
            }
            messageContainer.getChildren().add(messageLabel);

    }

    private boolean isSenderMessage(String message) {
        return message.contains(txtDisplayName.getText());
    }

    private void appendMessage(String message, boolean isSenderMessage) {
        System.out.println("Appending message: " + message);
            Label messageLabel = new Label(message);

            if (isSenderMessage) {
                messageLabel.getStyleClass().add("sender-message");
                messageLabel.setText(message);
            } else {
                messageLabel.getStyleClass().add("other-message");
            }
            if (message.startsWith("Me: ")){
                messageLabel.setText(message.substring(4));
            }else {
                messageLabel.setText(txtDisplayName.getText() + ": " + message);
            }
            messageContainer.getChildren().add(messageLabel);
        System.out.println("Message appended to UI");

    }


    @FXML
    void btnImageOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());

                sendImageToServer(imageBytes);

                System.out.println("Selected Image File: " + selectedFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendImageToServer(byte[] imageBytes) {
        try {
            dataOutputStream.writeUTF("IMAGE_MARKER");
            dataOutputStream.flush();

            dataOutputStream.writeInt(imageBytes.length);
            dataOutputStream.write(imageBytes);
            dataOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnEmojiOnAction(ActionEvent event) {
    }

    @FXML
    void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();
        }
    }

    private void sendMessage(){
        String message = txtMsgField.getText();
        if (!message.isEmpty()) {
            try {
                System.out.println("Sending message: " + message);
                dataOutputStream.writeUTF("TEXT_MARKER");
                dataOutputStream.flush();
                dataOutputStream.writeUTF(message + "\n");
                dataOutputStream.flush();

                txtMsgField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setDisplayName(String displayName){
        txtDisplayName.setText(displayName);
    }
    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/login.fxml"));

        Scene scene = new Scene(rootNode);

        Stage stage = new Stage();
        stage.setTitle("Chatterbox");

        stage.setScene(scene);
        stage.show();
    }

    public void initUser(UserDto userDto) {
        this.displayName = userDto.getDisplayName();
        txtDisplayName.setText(displayName);
    }
}

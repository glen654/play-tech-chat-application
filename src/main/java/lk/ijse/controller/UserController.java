package lk.ijse.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;



public class UserController {
    @FXML
    private AnchorPane rootNode;
    @FXML
    private TextArea txtArea;

    @FXML
    private Label txtDisplayName;

    @FXML
    private TextField txtMsgField;

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;



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
        try {
            while(true) {
                System.out.println("Waiting for messages");
                String message = dataInputStream.readUTF();
                System.out.println("Received messages: " + message);
                Platform.runLater(() -> {
                    txtArea.appendText(message + "\n");
                    txtArea.setScrollTop(Double.MAX_VALUE);
                });
            }
        } catch (IOException e) {
                throw new RuntimeException(e);
        }

    }


    @FXML
    void btnImageOnAction(ActionEvent event) {

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
                dataOutputStream.writeUTF(message + "\n");
                dataOutputStream.flush();

                txtMsgField.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}

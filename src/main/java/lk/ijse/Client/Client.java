package lk.ijse.Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lk.ijse.controller.ClientFormController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

public class Client implements Runnable, Serializable {
    private final String name;
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    private ClientFormController clientFormController;

    public Client(String name) throws IOException {
        this.name = name;

        socket = new Socket("localhost", 3001);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());

        outputStream.writeUTF(name);
        outputStream.flush();
        try {
            loadScene(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadScene(String displayName) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/client_interface.fxml"));
        Parent parent = loader.load();
        clientFormController = loader.getController();
        clientFormController.setDisplayName(displayName);
        clientFormController.setClient(this);
        stage.setResizable(false);
        stage.setScene(new Scene(parent));
        stage.setTitle(name + "'s Chat");
        stage.show();

        stage.setOnCloseRequest(event->{
            try {
                inputStream.close();
                outputStream.close();

                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        new Alert(Alert.AlertType.INFORMATION, "Welcome to Chatterbox").show();

        stage.setOnCloseRequest(event -> {
            try {
                outputStream.writeUTF(" Has left the chat!");
                outputStream.flush();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(String msg) throws IOException {
        outputStream.writeUTF(msg);
        outputStream.flush();
    }

    public void sendImage(byte[] bytes) throws IOException {
        System.out.println(bytes);
        outputStream.writeUTF("*image*");
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {
        try {
            outputStream.writeUTF("-New Member Joined to Chat-");
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                String message = inputStream.readUTF();
                if (message.equals("*image*")) {
                    receiveImage();
                } else {
                    clientFormController.writeMessage(message);
                }
            } catch (IOException e) {
                try {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                } catch (IOException ignored) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void receiveImage() throws IOException {
        String utf = inputStream.readUTF();
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        clientFormController.setImage(bytes, utf);
    }
}

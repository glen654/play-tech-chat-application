package lk.ijse.server;


import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lk.ijse.controller.ClientFormController;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    public static final List<ClientHandler> clients = new ArrayList<>();
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final String clientName;
    private ClientFormController clientFormController;
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        clientName = inputStream.readUTF();
        clients.add(this);
    }

    @Override
    public void run() {
        l1: while (socket.isConnected()) {
            try {
                String message = inputStream.readUTF();
                if (message.equals("*image*")) {
                    receiveImage();
                } else {
                    for (ClientHandler handler : clients) {

                        System.out.println(handler.clientName);
                        System.out.println(clientName);
                        if (!handler.clientName.equals(clientName)) {
                            handler.sendMessage(clientName, message);
                        }
                    }
                }
            } catch (IOException e) {

                clients.remove(this);
                break;
            }
        }
    }


    private void sendMessage(String sender, String msg) throws IOException {
        outputStream.writeUTF(sender + ": " + msg);
        outputStream.flush();
    }

    private void receiveImage() throws IOException {
        int size = inputStream.readInt();
        byte[] bytes = new byte[size];
        inputStream.readFully(bytes);
        for (ClientHandler handler : clients) {
            if (!handler.clientName.equals(clientName)) {
                handler.sendImage(clientName, bytes);
            }
        }
    }


    private void sendImage(String sender, byte[] bytes) throws IOException {
        outputStream.writeUTF("*image*");
        outputStream.writeUTF(sender);
        outputStream.writeInt(bytes.length);
        outputStream.write(bytes);
        outputStream.flush();
    }
}

package lk.ijse.server;


import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private Socket socket;

    private ArrayList<ClientHandler> clients;

    private BufferedReader reader;

    private PrintWriter writer;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private VBox messageContainer;
    private boolean isImage;
    private byte[] imageBytes;


    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients) {
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server thread waiting for messages");
            String message;
            while ((message = reader.readLine()) != null){
                System.out.println("Receieved on server: " + message);
                for (ClientHandler client : clients){
                        client.writer.println(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //writer.close();
                reader.close();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
       /* try {
            System.out.println("Server thread waiting for messages");
            String marker;
            while ((marker = reader.readLine()) != null) {
                System.out.println("Received marker: " + marker);
                if ("TEXT_MARKER".equals(marker)) {
                    String message = reader.readLine();
                    System.out.println("recived text message: " + message);
                    broadcastTextMessage(message);
                } else if ("IMAGE_MARKER".equals(marker)) {
                    receiveImageFromClient();
                    broadcastImageToClients();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void broadcastImageToClients() {
        for (ClientHandler client : clients) {
            if (client != this) {
                client.writer.println("IMAGE_MARKER");
                sendImageToClient(client, imageBytes);
            }
        }
    }

    private void receiveImageFromClient() {
        try {
            int imageLength = dataInputStream.readInt();
            byte[] imageBytes = new byte[imageLength];
            dataInputStream.readFully(imageBytes);

            processImageData(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendImageToClient(ClientHandler client, byte[] imageBytes) {
        try {
            client.dataOutputStream.writeInt(imageBytes.length);
            client.dataOutputStream.write(imageBytes);
            client.dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processImageData(byte[] imageBytes) {
        Platform.runLater(() -> {
            try {
                Image image = new Image(new ByteArrayInputStream(imageBytes));

                ImageView imageView = new ImageView(image);

                messageContainer.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void broadcastTextMessage(String message) {
        for (ClientHandler client : clients) {
            if (client != this) {
                //client.writer.println("TEXT_MARKER");
                client.writer.println(message);
                //client.writer.flush();
            }
        }
    }

}

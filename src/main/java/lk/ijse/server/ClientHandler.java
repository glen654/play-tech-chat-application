package lk.ijse.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private Socket socket;

    private ArrayList<ClientHandler> clients;

    private BufferedReader reader;

    private PrintWriter writer;


    public ClientHandler(Socket socket, ArrayList<ClientHandler> clients){
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(),true);
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
                writer.close();
                reader.close();
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

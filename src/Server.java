package src;

import javafx.scene.layout.GridPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            System.out.println("Connected");
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Error creating server.");
            e.printStackTrace();
        }
    }

    public void receiveMessage(GridPane root) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()) {
                    try {
                        String message = bufferedReader.readLine();
                        Controller.handleMessage(message, root);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from backend");
                        closeEverything(socket, bufferedReader);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

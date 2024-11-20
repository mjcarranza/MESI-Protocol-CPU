import java.io.*;
import java.net.*;

public class SocketHandler {
    private static final int PORT = 8888;
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static BufferedWriter writer;

    static {
        try {
            // Inicializa el ServerSocket y espera conexiones
            // serverSocket = new ServerSocket(PORT);
            System.out.println("Esperando conexiones en el puerto " + PORT + "...");
            clientSocket = new Socket("localhost", PORT); // Acepta la primera conexión
            System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

            // Configura los streams de entrada y salida
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Error inicializando el socket: " + e.getMessage());
        }
    }

    public static String receiveMessage() throws IOException {
        if (reader != null) {
            return reader.readLine(); // Espera y lee un mensaje del cliente
        }
        throw new IOException("El lector no está inicializado.");
    }

    public static void sendMessage(String message) throws IOException {
        if (writer != null) {
            writer.write(message);
            writer.newLine(); // Añade un salto de línea para completar el mensaje
            writer.flush(); // Asegura que el mensaje se envíe inmediatamente
        } else {
            throw new IOException("El escritor no está inicializado.");
        }
    }

    public static void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null) clientSocket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Error cerrando el socket: " + e.getMessage());
        }
    }
}

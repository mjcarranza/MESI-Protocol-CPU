import java.io.*;
import java.net.*;

public class SocketHandler {
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;

    // Método para conectar al servidor
    public void conectar(String host, int puerto) {
        try {
            socket = new Socket(host, puerto);
            System.out.println("Conectado al servidor: " + host + ":" + puerto);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para enviar mensajes al servidor
    public void enviarMensaje(String mensaje) {
        if (salida != null) {
            salida.println(mensaje);
            try {
                // Leer respuesta del servidor
                String respuesta = entrada.readLine();
                System.out.println("Respuesta del servidor: " + respuesta);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para cerrar la conexión
    public void cerrar() {
        try {
            if (entrada != null)
                entrada.close();
            if (salida != null)
                salida.close();
            if (socket != null)
                socket.close();
            System.out.println("Conexión cerrada.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

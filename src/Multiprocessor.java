import java.io.IOException;

public class Multiprocessor {
    private PE[] processors = new PE[4];
    private final Object stepperLock = new Object(); // Bloqueo compartido

    public void loadPrograms(String[][] programs) {
        // Instancia y carga cada PE con su propio programa
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new PE((byte) i, stepperLock);
            processors[i].loadProgram(programs[i]);
        }
        new Memory();
    }

    public void execute() {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(processors[i]);
            threads[i].start(); // Inicia cada PE en su propio hilo
        }

        while (true) {
            try {
                SocketHandler.receiveMessage(); // Recibe mensaje del cliente

                synchronized (stepperLock) {
                    stepperLock.notifyAll(); // Permite que todos los PEs avancen
                }
            } catch (IOException e) {
                System.err.println("Error en la comunicación por socket: " + e.getMessage());
            }
        }
    }
}

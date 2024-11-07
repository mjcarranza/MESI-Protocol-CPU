import java.util.Scanner;

public class Multiprocessor {
    private long[] sharedMemory;
    private PE[] processors;
    private final Object stepperLock = new Object();  // Bloqueo compartido

    public Multiprocessor() {
        this.sharedMemory = new long[256];  // Memoria compartida
        this.processors = new PE[4];  // Arreglo de PEs
    }

    public void loadPrograms(String[][] programs) {
        // Instancia y carga cada PE con su propio programa
        for (int i = 0; i < 4; i++) {
            processors[i] = new PE(sharedMemory, "PE" + (i + 1),stepperLock);
            processors[i].loadProgram(programs[i]);
        }
    }
    /* 
    public void execute() {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(processors[i]);
            threads[i].start();  // Inicia cada hilo para cada PE
        }

        // Espera a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Todos los procesadores han terminado su ejecución.");
    }*/
    public void execute() {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(processors[i]);
            threads[i].start();  // Inicia cada PE en su propio hilo
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Presiona Enter para avanzar todos los PEs");
            scanner.nextLine();  // Espera la entrada del usuario

            synchronized (stepperLock) {
                stepperLock.notifyAll();  // Permite que todos los PEs avancen
            }
        }
    }
}

import java.util.Scanner;

public class Multiprocessor {
    private PE[] processors = new PE[4];
    private final Object stepperLock = new Object(); // Bloqueo compartido
    private Memory memory;

    public void loadPrograms(String[][] programs) {
        // Instancia y carga cada PE con su propio programa
        for (int i = 0; i < processors.length; i++) {
            processors[i] = new PE((byte) (i + 1), stepperLock);
            processors[i].loadProgram(programs[i]);
        }
        memory = new Memory();
        memory.getMemoryInfo();
    }

    public void execute() {
        Thread[] threads = new Thread[4];
        for (int i = 0; i < 4; i++) {
            threads[i] = new Thread(processors[i]);
            threads[i].start(); // Inicia cada PE en su propio hilo
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Presiona Enter para avanzar todos los PEs");
            scanner.nextLine(); // Espera la entrada del usuario

            synchronized (stepperLock) {
                stepperLock.notifyAll(); // Permite que todos los PEs avancen
                memory.getMemoryInfo();
                for (int i = 0; i < 4; i++) {
                    processors[i].getCache().getCacheInfo();
                }
                Statistics.getStatisticsInfo();
            }

        }
    }
}

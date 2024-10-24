public class Main {
    public static void main(String[] args) {
        long[] sharedMemory = new long[256];  // Memoria compartida
        PE processor = new PE(sharedMemory);  // Instancia de PE

        // Cargar un programa simple en el PE
        String[] program = {
            "LOAD REG0 10",
            "INC REG0",
            "STORE REG0 11",
            "DEC REG0",
            "JNZ 6",
            "STORE REG1 100",
            "STORE REG2 25"
        };

        processor.loadProgram(program);
        processor.execute();  // Ejecutar el programa
        System.out.println("listooooo");
    }
}

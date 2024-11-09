/*public class Main {
    public static void main(String[] args) { 
        long[] sharedMemory = new long[256];  // Memoria compartida
        PE processor = new PE(sharedMemory);  // Instancia de PE

        // Cargar un programa simple en el PE
        String[] program = {
            "LOAD REG0 10",
            "INC REG0",
            "INC REG0",
            "INC REG0",
            "STORE REG0 11",
            "label1_",
            "DEC REG0",
            "JNZ REG0 label1_",
            "STORE REG1 100",
            "STORE REG2 25"
        };

        processor.loadProgram(program);
        processor.execute();  // Ejecutar el programa
        System.out.println("Fin");






        
    }
}
*/
public class Main {
    public static void main(String[] args) {
        Multiprocessor multiprocessor = new Multiprocessor();

        // Carga programas para cada PE
        String[][] programs = {
            {
                "LOAD REG0 10",
                "INC REG0",
                "INC REG0",
                "INC REG0",
                "STORE REG0 11",
                "label1_",
                "DEC REG0",
                "JNZ REG0 label1_",
                "STORE REG1 100",
                "STORE REG2 25"
            },
            {
                "LOAD REG0 10",
                "INC REG0",
                "INC REG0",
                "INC REG0",
                "STORE REG0 11",
                "label1_",
                "DEC REG0",
                "JNZ REG0 label1_",
                "STORE REG1 100",
                "STORE REG2 25"
            },
            {
                "LOAD REG0 10",
                "INC REG0",
                "INC REG0",
                "INC REG0",
                "STORE REG0 11",
                "label1_",
                "DEC REG0",
                "JNZ REG0 label1_",
                "STORE REG1 100",
                "STORE REG2 25"
            },
            {
                "LOAD REG0 10",
                "INC REG0",
                "INC REG0",
                "INC REG0",
                "STORE REG0 11",
                "label1_",
                "DEC REG0",
                "JNZ REG0 label1_",
                "STORE REG1 100",
                "STORE REG2 25",
                "INC REG1",

            }
        };

        multiprocessor.loadPrograms(programs);
        multiprocessor.execute();
    }
}


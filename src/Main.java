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

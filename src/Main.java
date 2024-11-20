/* 
public class Main {
    public static void main(String[] args) {
        Multiprocessor multiprocessor = new Multiprocessor();

        // Load programs from file
        //String[][] programs = loadProgramsFromFile("programs.txt");
        
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
}*/
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Multiprocessor multiprocessor = new Multiprocessor();

        // Load programs from file
        String[][] programs = loadProgramsFromFile("programs.txt");

        multiprocessor.loadPrograms(programs);
        multiprocessor.execute();
    }

    private static String[][] loadProgramsFromFile(String filename) {
        List<List<String>> programsList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<String> currentProgram = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    // Add current program to programs list
                    if (!currentProgram.isEmpty()) {
                        programsList.add(new ArrayList<>(currentProgram));
                        currentProgram.clear();
                    }
                } else {
                    currentProgram.add(line);
                }
            }

            // Add the last program if file doesn't end with ---
            if (!currentProgram.isEmpty()) {
                programsList.add(new ArrayList<>(currentProgram));
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        // Convert List<List<String>> to String[][]
        String[][] programs = new String[programsList.size()][];
        for (int i = 0; i < programsList.size(); i++) {
            programs[i] = programsList.get(i).toArray(new String[0]);
        }

        return programs;
    }
}

import java.util.Scanner;
public class PE implements Runnable {
    private long[] registers;
    private long[] sharedMemory;
    private int programCounter;
    private String[] program;
    private String name;
    private final Object stepperLock;  // Bloqueo compartido entre PEs y Multiprocessor
    //private Scanner scanner;

    public PE(long[] sharedMemory,String name,Object stepperLock) {
        this.registers = new long[4]; // Inicializa los registros
        this.sharedMemory = sharedMemory;
        this.programCounter = 0;
        this.name = name;
        //this.scanner = new Scanner(System.in);
        this.stepperLock = stepperLock;
    }

    public void loadProgram(String[] program) {
        this.program = program;
    }

    public void execute() {
        while (programCounter < program.length) {
            //System.out.println("Presiona Enter para ejecutar la instrucción " + programCounter);
            //scanner.nextLine();  // Espera a que el usuario presione Enter
            System.out.println(programCounter);
            synchronized (stepperLock) {
                try {
                    // Espera la señal del Multiprocessor para continuar
                    stepperLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(program.length);
            String instruction = program[programCounter];
            executeInstruction(instruction);
        }
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String opcode = parts[0];

        switch (opcode) {
            case "LOAD":
                int regNum = Integer.parseInt(parts[1].substring(3));
                int addr = Integer.parseInt(parts[2]);
                registers[regNum] = sharedMemory[addr];
                System.out.println("0x"+addr +" (" + sharedMemory[addr] +") >>> REG"+regNum);
                break;
            case "STORE":
                regNum = Integer.parseInt(parts[1].substring(3));
                addr = Integer.parseInt(parts[2]);
                sharedMemory[addr] = registers[regNum];
                System.out.println("REG"+regNum+" (" + registers[regNum] +") >>> 0x"+ addr);
                break;
            case "INC":
                regNum = Integer.parseInt(parts[1].substring(3));
                registers[regNum]++;
                long r = registers[regNum]-1;
                System.out.println("REG"+regNum+" ("+r+") = "+registers[regNum]);
                break;
            case "DEC":
                regNum = Integer.parseInt(parts[1].substring(3));
                registers[regNum]--;
                long r2 = registers[regNum]+1;
                System.out.println("REG"+regNum+" ("+r2+") = "+registers[regNum]);
                break;
            case "JNZ":
                regNum = Integer.parseInt(parts[1].substring(3));
                
                if (registers[regNum] != 0){
                    for (int i = 0; i < program.length; i++) {
                        if (parts[2].equals(program[i])){
                            //System.out.println("YAAAAYYYY $"+program[i]+"$"+i);
                            System.out.println("REG"+regNum+" ("+registers[regNum]+") != 0 >>> "+program[i]);
                            programCounter = i;
                            break;
                        }
                    }
                    //programCounter = 5;
                } else {
                    System.out.println("REG"+regNum+" ("+registers[regNum]+") == 0 >>> continue");
                }
                //programCounter = Integer.parseInt(parts[1]);
                break;
            default:
                if (parts[0].endsWith("_")){
                    System.out.println("Label \""+instruction+"\" detectado en instrucción " + programCounter);
                } else {
                    System.out.println("Instrucción no reconocida: " + instruction);
                }
        }
        programCounter++;
    }

    @Override
    public void run() {
        System.out.println("Iniciando " + name);
        execute();  // Método de ejecución de instrucciones
        System.out.println("Finalizando " + name);
    }
}

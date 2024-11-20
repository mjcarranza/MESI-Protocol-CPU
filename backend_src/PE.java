public class PE implements Runnable {
    private long[] registers = new long[4]; // Inicializa los registros
    private int programCounter;
    private String[] program;
    private byte id;
    private final Object stepperLock; // Bloqueo compartido entre PEs y Multiprocessor

    private Cache cache;

    public PE(byte id, Object stepperLock) {
        cache = new Cache(id);
        this.id = id;
        this.stepperLock = stepperLock;
    }

    public void loadProgram(String[] program) {
        this.program = program;
    }

    public void execute() {
        while (programCounter < program.length) {
            System.out.println(programCounter);
            synchronized (stepperLock) {
                try {
                    // Espera la señal del Multiprocessor para continuar
                    stepperLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // System.out.println(program.length);
            String instruction = program[programCounter];
            executeInstruction(instruction);
        }
    }

    private void executeInstruction(String instruction) {
        String[] parts = instruction.split(" ");
        String opcode = parts[0];

        switch (opcode) {
            case "LOAD":
            case "STORE":
                synchronized (Bus.getMemoryAccessLock()) {
                    int regNum = Integer.parseInt(parts[1].substring(3));
                    int addr = Integer.parseInt(parts[2]);
                    if (opcode.equals("LOAD")) {
                        read((short) addr);
                        CacheLine cacheLineSearched = cache.searchAddressInCache((short) addr, false);
                        registers[regNum] = cacheLineSearched.getData();
                        System.out.println("0x" + addr + " (" + cacheLineSearched.getData() + ") >>> REG" + regNum);
                    } else { // STORE
                        write((short) addr, registers[regNum]);
                        System.out.println("REG" + regNum + " (" + registers[regNum] + ") >>> 0x" + addr);
                    }
                }
                break;
            case "INC":
                int regNumInc = Integer.parseInt(parts[1].substring(3));
                registers[regNumInc]++;
                long r = registers[regNumInc] - 1;
                System.out.println("REG" + regNumInc + " (" + r + ") = " + registers[regNumInc]);
                break;
            case "DEC":
                int regNumDec = Integer.parseInt(parts[1].substring(3));
                registers[regNumDec]--;
                long r2 = registers[regNumDec] + 1;
                System.out.println("REG" + regNumDec + " (" + r2 + ") = " + registers[regNumDec]);
                break;
            case "JNZ":
                int regNumJnz = Integer.parseInt(parts[1].substring(3));
                if (registers[regNumJnz] != 0) {
                    for (int i = 0; i < program.length; i++) {
                        if (parts[2].equals(program[i])) {
                            System.out.println(
                                    "REG" + regNumJnz + " (" + registers[regNumJnz] + ") != 0 >>> " + program[i]);
                            programCounter = i;
                            break;
                        }
                    }
                } else {
                    System.out.println("REG" + regNumJnz + " (" + registers[regNumJnz] + ") == 0 >>> continue");
                }
                break;
            default:
                if (parts[0].endsWith("_")) {
                    System.out.println("Label \"" + instruction + "\" detectado en instrucción " + programCounter);
                } else {
                    System.out.println("Instrucción no reconocida: " + instruction);
                }
        }
        programCounter++;
    }

    @Override
    public void run() {
        System.out.println("Iniciando PE " + id);
        execute(); // Método de ejecución de instrucciones
        System.out.println("Finalizando PE " + id);
    }

    public void read(short address) {
        cache.readCacheLine(address, false);
    }

    public void write(short address, long data) {
        cache.writeCacheLine(address, data);
    }

    public Cache getCache() {
        return cache;
    }

}

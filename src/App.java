public class App {
    //Bus bus = new Bus();
    CPU cpu[] = new CPU[3];
    Memory memory;

    public App() {
        for (byte i = 0; i < cpu.length; i++) {
            cpu[i] = new CPU();
        }
        memory = new Memory();
        
        memory.getMemoryInfo();
        cpu[0].getCache().getCacheInfo();
        cpu[1].getCache().getCacheInfo();
        cpu[2].getCache().getCacheInfo();

        cpu[0].write((byte) 1, 1);
        cpu[1].read((byte) 1);
        cpu[2].write((byte) 1, 2);

        memory.getMemoryInfo();
        cpu[0].getCache().getCacheInfo();
        cpu[1].getCache().getCacheInfo();
        cpu[2].getCache().getCacheInfo();

    }
}

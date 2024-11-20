public class Memory implements BusObserver {

    private byte memoryLenght = 4;
    private byte memoryIndex = 0;
    private MemoryLine[] memoryLine = new MemoryLine[memoryLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isDataUpdateNeeded = true;
    private int millisecondsSearchingMemoryLine = 500;

    public Memory() {
        for (byte i = 0; i < memoryLenght; i++) {
            memoryLine[i] = new MemoryLine();
        }
        Bus.addBusObserver(this);
    }

    private MemoryLine searchAddressInMemory(byte address, boolean isForSimulation) {
        int milliseconds = 0;

        for (byte i = 0; i < memoryLenght; i++) {
            milliseconds += millisecondsSearchingMemoryLine;
            if (memoryLine[i].getAddress() == address) {
                if (isForSimulation) {
                    SimulationHandler.simulateBehavior(null, milliseconds);
                }
                return memoryLine[i];
            }
        }
        System.out.println("Error: Memory Line not found");
        System.exit(0);
        return null;
    }

    private void addAddressToMemory(byte address) {
        if (memoryIndex == memoryLenght) {
            System.out.println("Error: Not enough memory space");
            System.exit(0);
        }
        memoryLine[memoryIndex].setAddress(address);
        memoryIndex++;
    }

    @Override
    public void addressBusUpdate() {
        if (isAddressUpdateNeeded) {
            addAddressToMemory(Bus.getAddress());
            MemoryLine memoryLineSearched = searchAddressInMemory(Bus.getAddress(), true);

            isDataUpdateNeeded = false;
            Bus.setData(memoryLineSearched.getData(), (byte) -1);
            isDataUpdateNeeded = true;

            Bus.setShared(false, (byte) -1);
        }
        isAddressUpdateNeeded = true;

    }

    @Override
    public void dataBusUpdate() {
        if (isDataUpdateNeeded) {
            MemoryLine memoryLineSearched = searchAddressInMemory(Bus.getAddress(), false);
            memoryLineSearched.setData(Bus.getData());
        }
    }

    @Override
    public void sharedBusUpdate() {
        isAddressUpdateNeeded = false;
    }

    public void getMemoryInfo() {
        for (byte i = 0; i < memoryLenght; i++) {
            System.out.print(memoryLine[i].getAddress() + " | ");
            System.out.println(memoryLine[i].getData());
        }
        System.out.println("-------------------");
    }
}

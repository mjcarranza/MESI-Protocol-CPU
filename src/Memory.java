public class Memory implements BusObserver{
    
    private byte memoryLenght = 4;
    private byte memoryIndex = 0;
    private MemoryLine[] memoryLine = new MemoryLine[memoryLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isDataUpdateNeeded = true;

    public Memory(){
        for (byte i = 0; i < memoryLenght; i++) {
            memoryLine[i] = new MemoryLine();
        }
        Bus.addBusObserver(this);

    }

    private MemoryLine searchAddressInMemory(byte address){
        for (byte i = 0; i < memoryLenght; i++) {
            if (memoryLine[i].getAddress() == address){
                return memoryLine[i];
            }
        }
        return null;
    }

    private void addAddressToMemory(byte address){
        memoryLine[memoryIndex].setAddress(address);

        memoryIndex++;
        if (memoryIndex == memoryLenght) {
            System.out.println("Error: Not enough memory space");
            System.exit(0);
        }
    }

    @Override
    public void addressBusUpdate() {
        if (isAddressUpdateNeeded) {
            MemoryLine memoryLineSearched = searchAddressInMemory(Bus.getAddress());

            if(memoryLineSearched == null){
                addAddressToMemory(Bus.getAddress());
                memoryLineSearched = searchAddressInMemory(Bus.getAddress());  
            }

            System.out.println("Memoria: Revisando memoria...");
            getMemoryInfo();

            isDataUpdateNeeded = false;
            System.out.println("Memoria: Compartiendo al bus de datos...");
            Bus.setData(memoryLineSearched.getData());
            isDataUpdateNeeded = true;
            System.out.println("Memoria: Utilizando el bus de compartidos...");
            Bus.setShared(false);
        }
        isAddressUpdateNeeded = true;    
    }

    @Override
    public void dataBusUpdate() {
        if (isDataUpdateNeeded) {
            MemoryLine memoryLineSearched = searchAddressInMemory(Bus.getAddress());
            memoryLineSearched.setData(Bus.getData());
            System.out.println("Memoria: Actualizando datos en memoria...");
            getMemoryInfo();
            isAddressUpdateNeeded = false; 
        }       
    }

    @Override
    public void sharedBusUpdate() {
       
    }

    public void getMemoryInfo() {
        for (byte i = 0; i < memoryLenght; i++) {
            System.out.print(memoryLine[i].getAddress() + " | ");
            System.out.println(memoryLine[i].getData());
        }
        System.out.println("-------------------");
    }
}

public class MemoryLine {

    private byte address;
    private int data = 0;
    private int millisecondsEditingMemoryLine = 500;

    // Setters
    public void setAddress(byte address) {
        this.address = address;
        SimulationHandler.simulateBehavior("M: " + getAddress() + "|" + getData(),
                millisecondsEditingMemoryLine);
    }

    public void setData(int data) {
        this.data = data;
        SimulationHandler.simulateBehavior("M: " + getAddress() + "|" + getData(),
                millisecondsEditingMemoryLine);
    }

    // Getters
    public byte getAddress() {
        return address;
    }

    public int getData() {
        return data;
    }

}

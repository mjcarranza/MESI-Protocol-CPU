public class MemoryLine {

    private short address;
    private long data;
    private int millisecondsEditingMemoryLine = 1000;

    // Setters
    public void setAddress(short address) {
        this.address = address;
        SimulationHandler.simulateBehavior("M: " + getAddress() + "|" + getData(),
                millisecondsEditingMemoryLine);
    }

    public void setData(long data) {
        this.data = data;
        SimulationHandler.simulateBehavior("M: " + getAddress() + "|" + getData(),
                millisecondsEditingMemoryLine);
    }

    // Getters
    public short getAddress() {
        return address;
    }

    public long getData() {
        return data;
    }

}

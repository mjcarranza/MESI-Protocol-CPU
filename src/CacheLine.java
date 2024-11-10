public class CacheLine {

    private byte cacheLineID;
    private char state = 'I';
    private byte address;
    private int data;
    private int millisecondsEditingCacheLine = 500;

    public CacheLine(byte cacheLineID) {
        this.cacheLineID = cacheLineID;
    }

    // Setters
    public void setState(char state) {
        this.state = state;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setAddress(byte address) {
        this.address = address;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setData(int data) {
        this.data = data;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setAll(char state, byte address, int data) {
        this.state = state;
        this.address = address;
        this.data = data;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    // Getters
    public char getState() {
        return state;
    }

    public byte getAddress() {
        return address;
    }

    public int getData() {
        return data;
    }

}

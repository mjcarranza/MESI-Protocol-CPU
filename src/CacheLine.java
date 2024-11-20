public class CacheLine {

    private byte cacheLineID;
    private char state = 'I';
    private short address;
    private long data;
    private int millisecondsEditingCacheLine = 1000;

    public CacheLine(byte cacheLineID) {
        this.cacheLineID = cacheLineID;
    }

    // Setters
    public void setState(char state) {
        this.state = state;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setAddress(short address) {
        this.address = address;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setData(long data) {
        this.data = data;
        SimulationHandler.simulateBehavior("C" + cacheLineID + ": " + getState() + "|" + getAddress() + "|" + getData(),
                millisecondsEditingCacheLine);
    }

    public void setAll(char state, short address, long data) {
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

    public short getAddress() {
        return address;
    }

    public long getData() {
        return data;
    }

}

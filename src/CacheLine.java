public class CacheLine {

    private char state = 'I';
    private byte address;
    private int data;

    // Setters
    public void setState(char state) {
        this.state = state;
    }

    public void setAddress(byte address) {
        this.address = address;
    }
    
    public void setData(int data) {
        this.data = data;
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

public class MemoryLine {
 
    private byte address;
    private int data = 0;

    // Setters
    public void setAddress(byte address) {
        this.address = address;
    }
    
    public void setData(int data) {
        this.data = data;
    }

    // Getters
    public byte getAddress() {
        return address;
    }
    public int getData() {
        return data;
    }
    

}

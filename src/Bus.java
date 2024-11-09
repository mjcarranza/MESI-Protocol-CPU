public class Bus {
    private static int data = 0;
    private static byte address = 0;
    private static boolean shared = false;

    private static byte ObserversNumber = 4;
    private static byte ObserversConnected = 0;
    private static BusObserver[] busObservers = new BusObserver[ObserversNumber];

    public static void addBusObserver(BusObserver observer) {
        if (ObserversConnected < ObserversNumber){
            busObservers[ObserversConnected] = observer;
            ObserversConnected++;
        }  
    }

    private static void notifyDataChange() {
        for(byte i = 0; i < ObserversNumber; i++){
            busObservers[i].dataBusUpdate();
        }
    }

    private static void notifyAddressChange() {
        for(byte i = 0; i < ObserversNumber; i++){
            busObservers[i].addressBusUpdate();
        }
    }

    private static void notifySharedChange() {
        for(byte i = 0; i < ObserversNumber; i++){
            busObservers[i].sharedBusUpdate();
        }
    }

    // Setters
    public static synchronized void setData(int data){
        Bus.data = data;
        notifyDataChange();
    }

    public static synchronized void setAddress(byte address){
        Bus.address = address;
        notifyAddressChange();
    }

    public static synchronized void setShared(boolean shared){
        Bus.shared = shared;
        notifySharedChange();
    }

    // Getters
    public static int getData(){
        return data;
    }

    public static byte getAddress(){
        return address;
    }

    public static boolean getShared(){
        return shared;
    }

}

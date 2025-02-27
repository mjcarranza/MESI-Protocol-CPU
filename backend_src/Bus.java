public class Bus {
    private static long data = 0;
    private static short address = 0;
    private static boolean shared = false;

    private static byte ObserversNumber = 5;
    private static byte ObserversConnected = 0;
    private static BusObserver[] busObservers = new BusObserver[ObserversNumber];

    private static int millisecondsUsingBus = 1000;

    private static final Object memoryAccessLock = new Object(); // Bloqueo para operaciones de memoria

    public static void addBusObserver(BusObserver observer) {
        if (ObserversConnected < ObserversNumber) {
            busObservers[ObserversConnected] = observer;
            ObserversConnected++;
        }
    }

    private static void notifyDataChange() {
        for (byte i = 0; i < ObserversNumber; i++) {
            busObservers[i].dataBusUpdate();
        }
    }

    private static void notifyAddressChange() {
        for (byte i = 0; i < ObserversNumber; i++) {
            busObservers[i].addressBusUpdate();
        }
    }

    private static void notifySharedChange() {
        for (byte i = 0; i < ObserversNumber; i++) {
            busObservers[i].sharedBusUpdate();
        }
    }

    // Setters
    public static void setData(long data, byte cacheID) {
        if (cacheID == -1) {
            SimulationHandler.simulateBehavior("BDM", millisecondsUsingBus);
        } else {
            SimulationHandler.simulateBehavior("BD" + cacheID, millisecondsUsingBus);
        }
        Bus.data = data;
        notifyDataChange();
    }

    public static void setAddress(short address, byte cacheID) {
        if (cacheID == -1) {
            SimulationHandler.simulateBehavior("BAM", millisecondsUsingBus);
        } else {
            SimulationHandler.simulateBehavior("BA" + cacheID, millisecondsUsingBus);
        }
        Bus.address = address;
        notifyAddressChange();
    }

    public static void setShared(boolean shared, byte cacheID) {
        if (cacheID == -1) {
            SimulationHandler.simulateBehavior("BSM", millisecondsUsingBus);
        } else {
            SimulationHandler.simulateBehavior("BS" + cacheID, millisecondsUsingBus);
        }
        Bus.shared = shared;
        notifySharedChange();
    }

    // Getters
    public static long getData() {
        return data;
    }

    public static short getAddress() {
        return address;
    }

    public static boolean getShared() {
        return shared;
    }

    public static Object getMemoryAccessLock() {
        return memoryAccessLock;
    }
}

public class Cache implements BusObserver {

    private byte cacheID;
    private byte cacheLenght = 2;
    private byte cacheIndex = 0;
    private CacheLine[] cacheLine = new CacheLine[cacheLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isDataUpdateNeeded = true;
    private boolean isSearchingOutside = false;
    private int millisecondsSearchingCacheLine = 500;

    public Cache(byte cacheID) {
        this.cacheID = cacheID;
        Bus.addBusObserver(this);

        for (byte i = 0; i < cacheLenght; i++) {
            cacheLine[i] = new CacheLine(cacheID);
        }
    }

    public void readCacheLine(byte address) {
        CacheLine cacheLineSearched = searchAddressInCache(address, true);
        if (cacheLineSearched == null) {
            addAddressToCache(address); // No existe
            cacheLineSearched = searchAddressInCache(address, false);
        }
        if (cacheLineSearched.getState() == 'I')
            searchAddressOutside(address); // Es Invalido
    }

    public CacheLine searchAddressInCache(byte address, boolean isForSimulation) {
        int milliseconds = 0;

        for (byte i = 0; i < cacheLenght; i++) {
            milliseconds += millisecondsSearchingCacheLine;
            if (cacheLine[i].getAddress() == address) {
                if (isForSimulation) {
                    SimulationHandler.simulateBehavior("Cache #" + cacheID + ": Cache hit!", milliseconds);
                    Statistics.addCacheHits();
                }
                return cacheLine[i];
            }
        }
        if (isForSimulation) {
            SimulationHandler.simulateBehavior("Cache #" + cacheID + ": Cache miss!", milliseconds);
            Statistics.addCacheMisses();
        }
        return null;
    }

    private void addAddressToCache(byte address) {
        cacheLine[cacheIndex].setAll('I', address, 0);
        cacheIndex = (cacheIndex == cacheLenght) ? 0 : cacheIndex++;
    }

    private void searchAddressOutside(byte address) {
        isSearchingOutside = true;
        isAddressUpdateNeeded = false;
        Bus.setAddress(address);
        isAddressUpdateNeeded = true;
    }

    public void writeCacheLine(byte address, int data) {
        readCacheLine(address);

        CacheLine cacheLineSearched = searchAddressInCache(address, false);
        if (cacheLineSearched.getData() != data) {
            cacheLineSearched.setData(data);
            if (cacheLineSearched.getState() == 'S') { // Otras caches tienen el dato
                isDataUpdateNeeded = false;
                Bus.setData(data);
                isDataUpdateNeeded = true;
                cacheLineSearched.setState('E');

            } else { // Solo esta cache tiene este address
                cacheLineSearched.setState('M');
            }
        }
    }

    @Override
    public void addressBusUpdate() {
        if (isAddressUpdateNeeded) {
            // La cache busca si tiene el address solicitado
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress(), true);

            if (cacheLineSearched != null) { // Tiene el address
                if (cacheLineSearched.getState() != 'I') { // El dato esta actualizado
                    isDataUpdateNeeded = false;
                    Bus.setData(cacheLineSearched.getData()); // En el bus de datos para que la memoria lo guarde
                    isDataUpdateNeeded = true;

                    cacheLineSearched.setState('S');
                    Bus.setShared(true);
                }
            }
        }
        isAddressUpdateNeeded = true;
    }

    @Override
    public void dataBusUpdate() {
        if (isDataUpdateNeeded) {
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress(), false);
            if (cacheLineSearched != null && cacheLineSearched.getData() != Bus.getData()
                    && cacheLineSearched.getState() != 'I' && !isSearchingOutside) { // Si tiene el dato desactualizado
                cacheLineSearched.setState('I');
            }
        }
    }

    @Override
    public void sharedBusUpdate() {
        isAddressUpdateNeeded = Bus.getShared() ? false : isAddressUpdateNeeded; // Si el shared se hizo por una cache,
                                                                                 // no es necesario revisar el address

        if (isSearchingOutside) { // Espera recibir el valor de un dato
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress(), false);
            cacheLineSearched.setData(Bus.getData());

            if (Bus.getShared()) { // Viene de otra cache
                cacheLineSearched.setState('S');
            } else { // Viene de memoria
                cacheLineSearched.setState('E');
            }
            isSearchingOutside = false;
        }
    }

    public void getCacheInfo() {
        System.out.println("Cache #" + cacheID + ": ");
        for (byte i = 0; i < cacheLenght; i++) {
            System.out.print(cacheLine[i].getState() + " | ");
            System.out.print(cacheLine[i].getAddress() + " | ");
            System.out.println(cacheLine[i].getData());
        }
        System.out.println("-------------------");
    }
}

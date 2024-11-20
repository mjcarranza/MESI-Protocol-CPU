public class Cache implements BusObserver {

    private byte cacheID;
    private byte cacheLenght = 8;
    private byte cacheIndex = 0;
    private CacheLine[] cacheLine = new CacheLine[cacheLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isDataUpdateNeeded = true;
    private boolean isSearchingOutside = false;
    private int millisecondsSearchingCacheLine = 250;

    public Cache(byte cacheID) {
        this.cacheID = cacheID;
        Bus.addBusObserver(this);

        for (byte i = 0; i < cacheLenght; i++) {
            cacheLine[i] = new CacheLine(cacheID);
        }
    }

    public void readCacheLine(short address, boolean isWriting) {
        CacheLine cacheLineSearched = searchAddressInCache(address, true);
        if (!isWriting)
            SimulationHandler.simulateBehavior("SR" + cacheID, 0);
        if (cacheLineSearched == null) {
            addAddressToCache(address); // No existe
            cacheLineSearched = searchAddressInCache(address, false);
        }
        if (cacheLineSearched.getState() == 'I')
            searchAddressOutside(address); // Es Invalido
    }

    public CacheLine searchAddressInCache(short address, boolean isForSimulation) {
        int milliseconds = 0;

        for (byte i = 0; i < cacheLenght; i++) {
            milliseconds += millisecondsSearchingCacheLine;
            if (cacheLine[i].getAddress() == address) {
                if (isForSimulation) {
                    SimulationHandler.simulateBehavior("SH" + cacheID, milliseconds);
                }
                return cacheLine[i];
            }
        }
        if (isForSimulation) {
            SimulationHandler.simulateBehavior("SM" + cacheID, milliseconds);
        }
        return null;
    }

    private void addAddressToCache(short address) {
        cacheLine[cacheIndex].setAll('I', address, 0);
        cacheIndex = (cacheIndex == cacheLenght - 1) ? 0 : (byte)(cacheIndex + 1);
    }

    private void searchAddressOutside(short address) {
        isSearchingOutside = true;
        isAddressUpdateNeeded = false;
        Bus.setAddress(address, cacheID);
        isAddressUpdateNeeded = true;
    }

    public void writeCacheLine(short address, long data) {
        readCacheLine(address, true);
        SimulationHandler.simulateBehavior("SW" + cacheID, 0);
        CacheLine cacheLineSearched = searchAddressInCache(address, false);
        if (cacheLineSearched.getData() != data) {
            cacheLineSearched.setData(data);
            if (cacheLineSearched.getState() == 'S') { // Otras caches tienen el dato
                isDataUpdateNeeded = false;
                Bus.setData(data, cacheID);
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
                    Bus.setData(cacheLineSearched.getData(), cacheID); // En el bus de datos para que la memoria lo guarde
                    isDataUpdateNeeded = true;

                    cacheLineSearched.setState('S');
                    Bus.setShared(true, cacheID);
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
                SimulationHandler.simulateBehavior("SI" + cacheID, 0);
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
}

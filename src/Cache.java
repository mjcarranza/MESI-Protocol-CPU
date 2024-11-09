public class Cache implements BusObserver {
    
    private byte cacheLenght = 2;
    private byte cacheIndex = 0;
    private CacheLine[] cacheLine = new CacheLine[cacheLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isDataUpdateNeeded = true;
    private boolean isSearchingOutside = false;


    public Cache() {
        for (byte i = 0; i < cacheLenght; i++) {
            cacheLine[i] = new CacheLine();  
        }
        Bus.addBusObserver(this);

    }

    public void readCacheLine(byte address){
        System.out.println("Cache: Leyendo cache...");
        CacheLine cacheLineSearched = searchAddressInCache(address);
        if (cacheLineSearched == null){ // No existe 
            System.out.println("Cache: Direccion no encontrada");
            addAddressToCache(address);
            searchAddressOutside(address);
        }
        else if (cacheLineSearched.getState() == 'I'){ // Es invalido
            System.out.println("Cache: Direccion invalida");
            searchAddressOutside(address);
        }

        System.out.println("Cache: Lectura finalizada");
        getCacheInfo();
        
    }

    private void searchAddressOutside(byte address) {
        isSearchingOutside = true;
        System.out.println("Cache: Escribiendo en el bus de direcciones...");
        isAddressUpdateNeeded = false;
        Bus.setAddress(address);
        isAddressUpdateNeeded = true;
    }

    public void writeCacheLine(byte address, int data){
        readCacheLine(address);

        System.out.println("Cache: Escrbiendo valor en cache...");
        CacheLine cacheLineSearched = searchAddressInCache(address);
        cacheLineSearched.setData(data);
        getCacheInfo();

        if (cacheLineSearched.getState() == 'S') { // Otras caches tienen el dato
            System.out.println("Cache: Dato en multiples caches");
            System.out.println("Cache: Compartiendo al bus de datos...");
            isDataUpdateNeeded = false;
            Bus.setData(data);
            isDataUpdateNeeded = true;
            cacheLineSearched.setState('E');
        }
        else { // Solo esta cache tiene este address
            System.out.println("Cache: Solo se encontro dato en esta cache");
            cacheLineSearched.setState('M');
        }

        System.out.println("Cache: Escrbiendo estado en cache...");
        getCacheInfo();

    }

    private void addAddressToCache(byte address){
        cacheLine[cacheIndex].setState('I');
        cacheLine[cacheIndex].setAddress(address);
        cacheLine[cacheIndex].setData(0);
        
        cacheIndex++;
        if (cacheIndex == cacheLenght) { // No hay espacio en cache
            cacheIndex = 0;
        }
        
    }

    private CacheLine searchAddressInCache(byte address){
        for (byte i = 0; i < cacheLenght; i++) {
            if (cacheLine[i].getAddress() == address){
                return cacheLine[i];
            }
        }
        return null;
    }

    @Override
    public void addressBusUpdate() {
        if (isAddressUpdateNeeded){  
            // La cache busca si tiene el address solicitado
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress());

            System.out.println("Cache: Revisando cache...");
            getCacheInfo();

            if(cacheLineSearched != null){ // Tiene el address
                if(cacheLineSearched.getState() != 'I'){ // El dato esta actualizado
                    System.out.println("Cache: Direccion encontrada");
                    isDataUpdateNeeded = false;
                    System.out.println("Cache: Compartiendo al bus de datos...");
                    Bus.setData(cacheLineSearched.getData()); // Se pone en el bus de datos para que la memoria lo guarde
                    isDataUpdateNeeded = true;
                    System.out.println("Cache: Actualizando estado a Shared...");
                    cacheLineSearched.setState('S');
                    System.out.println("Cache: Utilizando el bus de compartidos...");
                    Bus.setShared(true);
                }
            }
        }
        isAddressUpdateNeeded = true;
    }

    @Override
    public void dataBusUpdate() {
        if (isDataUpdateNeeded) {
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress());
            if (cacheLineSearched != null && cacheLineSearched.getData() != Bus.getData() && cacheLineSearched.getState() != 'I' && !isSearchingOutside) { // Si tiene el dato desactualizado
                cacheLineSearched.setState('I');

                System.out.println("Cache: Invalidando dato");
                getCacheInfo();
            }
        }
    }

    @Override
    public void sharedBusUpdate() {
        isAddressUpdateNeeded = false;

        if (isSearchingOutside) { // Espera recibir el valor de un dato
            CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress());
            
            cacheLineSearched.setData(Bus.getData());
            if (Bus.getShared()) { // Viene de otra cache
                System.out.println("Cache: Actualizando estado a Shared...");
                cacheLineSearched.setState('S');
            }
            else {  // Viene de memoria
                System.out.println("Cache: Actualizando estado a Exclusive...");
                cacheLineSearched.setState('E');
            }
            isSearchingOutside = false;

            getCacheInfo();
        }
    }

    public void getCacheInfo() {
        for (byte i = 0; i < cacheLenght; i++) {
            System.out.print(cacheLine[i].getState() + " | ");
            System.out.print(cacheLine[i].getAddress() + " | ");
            System.out.println(cacheLine[i].getData());
        }
        System.out.println("-------------------");
    }
}

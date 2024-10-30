public class Cache implements BusObserver {
    
    private byte cacheLenght = 2;
    private byte cacheIndex = 0;
    private CacheLine[] cacheLine = new CacheLine[cacheLenght];
    private boolean isAddressUpdateNeeded = true;
    private boolean isSearchingOutside = false;


    public Cache() {
        for (byte i = 0; i < cacheLenght; i++) {
            cacheLine[i] = new CacheLine();  
        }
        Bus.addBusObserver(this);

    }

    public void readCacheLine(byte address){
        System.out.println("Leyendo cache...");
        CacheLine cacheLineSearched = searchAddressInCache(address);
        if (cacheLineSearched == null){ // No existe 
            System.out.println("Address no encontrado...");
            addAddressToCache(address);
            searchAddressOutside(address);
        }
        else if (cacheLineSearched.getState() == 'I'){ // Es invalido
            System.out.println("Estado invalido...");
            searchAddressOutside(address);
        }

        System.out.println("Estado valido...");
        getCacheInfo();
        
    }

    private void searchAddressOutside(byte address) {
        isSearchingOutside = true;
        System.out.println("Escribiendo address en bus...");
        isAddressUpdateNeeded = false;
        Bus.setAddress(address);
        isAddressUpdateNeeded = true;
    }

    public void writeCacheLine(byte address, int data){
        readCacheLine(address);
        CacheLine cacheLineSearched = searchAddressInCache(address);
        cacheLineSearched.setData(data);
        System.out.println("Escrbiendo dato en cache (dato)...");
        getCacheInfo();

        if (cacheLineSearched.getState() == 'S') { // Otras caches tienen el dato
            System.out.println("Compartiendo con memoria e invalidando caches externas");
            Bus.setData(data);
            cacheLineSearched.setState('E');
        }
        else { // Solo esta cache tiene este address
            System.out.println("Solo se encontro dato en esta cache");
            cacheLineSearched.setState('M');
        }

        System.out.println("Escribiendo en cache (estado)...");
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

            System.out.println("Revisando cache...");
            getCacheInfo();

            if(cacheLineSearched != null){ // Tiene el address
                if(cacheLineSearched.getState() != 'I'){ // El dato esta actualizado, ToDo: no dejar que varias caches manden el dato si esta en estado S  
                    System.out.println("Address encontrado, compartiendo a memoria e invalidando en caches externas...");
                    Bus.setData(cacheLineSearched.getData()); // Se pone en el bus de datos para que la memoria lo guarde
                    System.out.println("Actualizar dato en caches a shared");
                    Bus.setShared(true);
                }
            }
        }
    }

    @Override
    public void dataBusUpdate() {
        CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress());
        if (cacheLineSearched != null) {
            cacheLineSearched.setState('I');

            System.out.println("Invalidando dato en cache...");
            getCacheInfo();
        }
    }

    @Override
    public void sharedBusUpdate() {
        CacheLine cacheLineSearched = searchAddressInCache(Bus.getAddress());
        
        System.out.println("Verificando actualizacion de cache...");
        getCacheInfo();

        if ((cacheLineSearched != null && cacheLineSearched.getData() == Bus.getData()) || isSearchingOutside) { // la cache tiene el mismo dato o lo quiere
            
            cacheLineSearched.setData(Bus.getData());
            if (Bus.getShared()) { // Viene de otra cache
                System.out.println("Actualizando dato a shared...");
                cacheLineSearched.setState('S');
            }
            else {  // Viene de memoria
                System.out.println("Actualizando dato a exclusive...");
                cacheLineSearched.setState('E');
            }
            isSearchingOutside = false;
        }

        System.out.println("Actualizacion finalizada de cache...");
        getCacheInfo();
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

public class CPU {

    private Cache cache = new Cache();

    public void read(byte address){
        cache.readCacheLine(address);
    }

    public void write(byte address, int data){
        cache.writeCacheLine(address, data);
    }

    public Cache getCache() {
        return cache;
    }

}

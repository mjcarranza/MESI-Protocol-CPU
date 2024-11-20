package src;

public class Statistic {
    private String name;
    private int invalidations;
    private int readRequests;
    private int writeRequest;

    public Statistic(String name, int invalidations, int readRequests, int writeRequest, int cacheMisses, int cacheHits) {
        this.name = name;
        this.invalidations = invalidations;
        this.readRequests = readRequests;
        this.writeRequest = writeRequest;
        this.cacheMisses = cacheMisses;
        this.cacheHits = cacheHits;
    }

    public int getReadRequests() {
        return readRequests;
    }

    public void setReadRequests(int readRequests) {
        this.readRequests = readRequests;
    }

    public int getWriteRequest() {
        return writeRequest;
    }

    public void setWriteRequest(int writeRequest) {
        this.writeRequest = writeRequest;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }

    public void setCacheMisses(int cacheMisses) {
        this.cacheMisses = cacheMisses;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public void setCacheHits(int cacheHits) {
        this.cacheHits = cacheHits;
    }

    private int cacheMisses;
    private int cacheHits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInvalidations() {
        return invalidations;
    }

    public void setInvalidations(int invalidations) {
        this.invalidations = invalidations;
    }
}

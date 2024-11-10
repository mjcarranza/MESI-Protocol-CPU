public class Statistics {

    private static int cacheMisses;
    private static int cacheHits;
    private static int milisecondsSearching;
    private static int searches;
    private static float averageSearchTime;

    // Adders
    public static void addCacheMisses() {
        cacheMisses++;
    }

    public static void addCacheHits() {
        cacheHits++;
    }

    public static void addTimeSearching(int miliseconds) {
        milisecondsSearching += miliseconds;
        searches++;
        averageSearchTime = milisecondsSearching / searches;

    }

    public static void getStatisticsInfo() {
        System.out.println("Cache misses: " + cacheMisses);
        System.out.println("Cache hits: " + cacheHits);
        System.out.println("Miliseconds searching: " + milisecondsSearching);
        System.out.println("Searches: " + searches);
        System.out.println("Average miliseconds searching: " + averageSearchTime);
    }

}

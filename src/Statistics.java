import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
        averageSearchTime = (float) milisecondsSearching / searches;
    }

    // Método para imprimir estadísticas en consola
    public static void getStatisticsInfo() {
        System.out.println("Cache misses: " + cacheMisses);
        System.out.println("Cache hits: " + cacheHits);
        System.out.println("Miliseconds searching: " + milisecondsSearching);
        System.out.println("Searches: " + searches);
        System.out.println("Average miliseconds searching: " + averageSearchTime);

        saveStatisticsToCSV("statistics.csv");
    }

    // Método para guardar estadísticas en un archivo CSV
    public static void saveStatisticsToCSV(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribir encabezados
            writer.write("Cache Misses,Cache Hits,Miliseconds Searching,Searches,Average Search Time");
            writer.newLine();

            // Escribir datos
            writer.write(cacheMisses + "," +
                    cacheHits + "," +
                    milisecondsSearching + "," +
                    searches + "," +
                    averageSearchTime);
            writer.newLine();

            System.out.println("Estadísticas guardadas en el archivo: " + fileName);

        } catch (IOException e) {
            System.err.println("Error al guardar el archivo CSV: " + e.getMessage());
        }
    }
}

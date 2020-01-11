import handlers.BatchWorker;
import handlers.TimeFormatter;

class App {
  public static void main(String[] args) {
    boolean verboseMode = true;
    int batchSize = 50000;
    String database = "imdb_data_test";
    String dataFolder = "data";

    long timestamp = System.currentTimeMillis();
    try {
      // Titles
      BatchWorker worker = new BatchWorker(verboseMode, batchSize, database, dataFolder);
      
      System.out.println("Starting data parsing...");
      worker.start(false);
    
    } catch (OutOfMemoryError e) {
      System.err.println("Used memory: " + getUsedMemory());
      e.printStackTrace();
    }
    System.out.println("All data inserted into db");
    System.out.println("Total time: " + TimeFormatter.format(System.currentTimeMillis() - timestamp));
  }


  private static String getUsedMemory() {
    return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Math.pow(1024, 2)) + " MB";
  }
}
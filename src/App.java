import handlers.BatchWorker;
import handlers.TimeFormatter;

class App {
  public static void main(String[] args) {
    // *** Settings - change if needed ***/
    boolean verboseMode = true;
    int batchSize = 50000;
    String database = "imdb_data";
    String dataFolder = "data";
    // *************************** //

    long timestamp = System.currentTimeMillis();
    try {
      BatchWorker worker = new BatchWorker(verboseMode, batchSize, database, dataFolder);
      
      System.out.println("Starting data parsing...");
      worker.start(true);
    
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
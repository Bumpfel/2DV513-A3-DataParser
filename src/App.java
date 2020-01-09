import handlers.BatchWorker;
import handlers.TimeFormatter;

class BatchApp {
  public static void main(String[] args) {
    boolean verboseMode = true;

    int batchSize = 50000;
        
    long timestamp = System.currentTimeMillis();
    try {
      // Titles
      BatchWorker worker = new BatchWorker(verboseMode, batchSize);
      
      System.out.println("Starting data parsing...");
      worker.start();
    
    } catch (OutOfMemoryError e) {
      System.out.println("used memory: " + getUsedMemory());
      e.printStackTrace();
    }
    System.out.println("All data inserted into db");
    System.out.println("Total time: " + TimeFormatter.format(System.currentTimeMillis() - timestamp));
  }


  private static String getUsedMemory() {
    return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Math.pow(1024, 2)) + " MB";
  }
}
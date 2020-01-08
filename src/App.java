import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import handlers.DBHandler;
import handlers.DataParser;
import model.IMDBData;

class App {
  public static void main(String[] args) {
    boolean verboseMode = false;

    int batchSize = 100000;
    DBHandler db = new DBHandler(verboseMode);
    db.connect("imdb_data");
      
    // clear old data
    db.exec("DELETE FROM titles");
    db.exec("DELETE FROM names");
    System.out.print(verboseMode ? "old data cleared from db\n\n" : "");
    
    System.out.println("Starting data parsing...");
    
    try {
      // Titles
      DataParser parser = new DataParser(verboseMode);
      Collection<IMDBData> titles = parser.parseTitles();
      System.out.println(titles.size() + "titles parsed");
      db.batchInsertion("titles", titles, batchSize);
      System.out.println("titles inserted into db");
      titles = null;
    
      Collection<IMDBData> names = parser.parseNames();
      System.out.println("names parsed");
      db.batchInsertion("names", names, batchSize);
      System.out.println("names inserted into db");
      
      names = null;
      // db.batchInsertion("professions", professions);
      // System.out.println("-professions inserted into db");

    } catch (OutOfMemoryError e) {
      System.out.println(getUsedMemory());
      e.printStackTrace();
    }
    System.out.println("All data inserted into db");
  }


  private static String getUsedMemory() {
    return (int) ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / Math.pow(1024, 2)) + " MB";
  }
}
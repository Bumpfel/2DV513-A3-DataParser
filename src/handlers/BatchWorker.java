package handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.Episode;
import model.IMDBData;
import model.Title;
import model.Name;

// TODO filtrera ut viss data för att minska datamängd. titleType='short'

public class BatchWorker {
  private String mDataFolder;
  private String mDatabase;
  private boolean mVerboseMode = false;
  private DBHandler mDB;
  private int mBatchSize;
  private enum SQLOperation { INSERT, UPDATE }

  public BatchWorker(boolean debug, int batchSize, String database, String dataFolder) {
    mVerboseMode = debug;
    
    mDatabase = database;
    mDataFolder = dataFolder;
    mBatchSize = batchSize;
    mDB = new DBHandler(true);
    mDB.connect(mDatabase);
  }
  
  public void start(boolean truncateData) {
    if(truncateData) {
      mDB.exec("DELETE FROM episodes");
      mDB.exec("DELETE FROM titles");
      mDB.exec("DELETE FROM names");
      System.out.print(mVerboseMode ? "old data cleared from db\n\n" : "");
    }

    parseTitlesAndRatings();
    parseNames();
    parseEpisodes();
  }

  private void parseTitlesAndRatings() {
    if(mVerboseMode) System.out.println("parsing titles...");
    int numParsedTitles = parseFileAndPutInDB("titles.tsv", Title.class, SQLOperation.INSERT);
    if(mVerboseMode) System.out.println(numParsedTitles + " titles parsed");
    if(mVerboseMode) System.out.println("parsing ratings...");
    int numParsedRatings = parseFileAndPutInDB("ratings.tsv", Title.class, SQLOperation.UPDATE);
    if(mVerboseMode) System.out.println(numParsedRatings + " ratings parsed");
  }

  private void parseNames() {
    if(mVerboseMode) System.out.println("parsing names...");
    int numParsedNames = parseFileAndPutInDB("names.tsv", Name.class, SQLOperation.INSERT);
    if(mVerboseMode) System.out.println(numParsedNames + " names parsed");
  }
  
  private void parseEpisodes() {
    if(mVerboseMode) System.out.println("parsing episodes...");
    int numParsedNames = parseFileAndPutInDB("episodes.tsv", Episode.class, SQLOperation.INSERT);
    if(mVerboseMode) System.out.println(numParsedNames + " episodes parsed");
  }

  private int parseFileAndPutInDB(String path, Class<?> clazz, SQLOperation sqlOperation) {
    Map<String, IMDBData> parsedObjects = new HashMap<>();
    int totalObjectsParsed = 0;
    try {
      File file = new File(mDataFolder + "/" + path);
      try (Scanner scanner = new Scanner(file)) {

        // record fields
        String[] fields = scanner.nextLine().split("\t");
        // System.out.println("Found fields: " + Arrays.toString(fields));
        Map<String, String> attributes = new HashMap<>();

        int batchSize = 0, batchNr = 0, totalBatches = 0;
        if(mVerboseMode) {
          totalBatches = getTotalBatches(file);
        }

        while (scanner.hasNextLine()) {
          String[] data = scanner.nextLine().split("\t");
          batchSize++;

          // map fields with data to allow safer object creation (order does not matter in constructor)
          for (int i = 0; i < fields.length; i++) {
            attributes.put(fields[i], data[i]);
          }
        
          IMDBData object = (IMDBData) clazz.getDeclaredConstructors()[0].newInstance(attributes); // instantiation through reflection

          parsedObjects.put(object.getId(), object);
          attributes.clear();

          if(batchSize >= mBatchSize || !scanner.hasNextLine()) {
            batchSize = 0;
            if(sqlOperation == SQLOperation.INSERT) {
              mDB.batchInsertion(clazz.getSimpleName() + "s", parsedObjects.values(), -1);
              if(mVerboseMode) System.out.println(" insert-batch #" + ++batchNr + " of " + totalBatches);
            } else if(sqlOperation == SQLOperation.UPDATE) {
              mDB.batchUpdate(clazz.getSimpleName() + "s", parsedObjects.values(), -1);
              if(mVerboseMode) System.out.println(" update-batch #" + ++batchNr + " of " + totalBatches);
            }
            totalObjectsParsed += parsedObjects.size();
            parsedObjects.clear();
          }
        }        
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }
    return totalObjectsParsed;
  }

  /**
   * Calculates total # or batches
   * Uses up some cpu power to count # lines in file. set verbose mode to false to disable
   */
  private int getTotalBatches(File file) {
    int totalBatches = 0;
    try(Scanner tempScanner = new Scanner(file)) {
      while(tempScanner.hasNextLine()) {
        tempScanner.nextLine();
        totalBatches++;
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
    return totalBatches / mBatchSize + 1;
  }

}

package handlers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.IMDBData;
import model.Title;
import model.Name;

public class BatchWorker {
  private String mDataPath = "data/";
  private String mDatabase = "2dv513a3";
  private boolean mVerboseMode = false;
  private DBHandler mDB;
  private int mBatchSize;
  private enum SQLOperation { INSERT, UPDATE }

  public BatchWorker(boolean debug, int batchSize) {
    mVerboseMode = debug;
    
    mBatchSize = batchSize;
    mDB = new DBHandler(true);
    mDB.connect(mDatabase);
    mDB.exec("TRUNCATE titles");
    mDB.exec("TRUNCATE names");
    System.out.print(mVerboseMode ? "old data cleared from db\n\n" : "");
  }

  public void start() {
    parseTitles();
    parseNames();
  }

  private void parseTitles() {
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

  private int parseFileAndPutInDB(String path, Class<?> dataClass, SQLOperation sqlOperation) {
    Map<String, IMDBData> parsedObjects = new HashMap<>();
    int totalObjectsParsed = 0;
    try {
      File file = new File(mDataPath + path);
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

          IMDBData object = null;
          if (dataClass == Title.class) {
            object = new Title(attributes);
          } else if (dataClass == Name.class) {
            object = new Name(attributes);
          } else {
            continue;
          }
          parsedObjects.put(object.getId(), object);
          attributes.clear();

          if(batchSize >= mBatchSize) { // TODO körs inte för sista batchen
            batchSize = 0;
            if(sqlOperation == SQLOperation.INSERT) {
              mDB.batchInsertion(dataClass.getSimpleName() + "s", parsedObjects.values(), -1);
              if(mVerboseMode) System.out.println(" insert-batch #" + ++batchNr + " of " + totalBatches);
            } else if(sqlOperation == SQLOperation.UPDATE) {
              mDB.batchUpdate(dataClass.getSimpleName() + "s", parsedObjects.values(), -1);
              if(mVerboseMode) System.out.println(" update-batch #" + ++batchNr + " of " + totalBatches);
            }
            totalObjectsParsed += parsedObjects.size();
            parsedObjects.clear();
          }
        }
        if(sqlOperation == SQLOperation.INSERT) { // TODO hemsk kodduplicering
          mDB.batchInsertion(dataClass.getSimpleName() + "s", parsedObjects.values(), -1);
          if(mVerboseMode) System.out.println(" insert-batch #" + ++batchNr + " of " + totalBatches);
        } else if(sqlOperation == SQLOperation.UPDATE) {
          mDB.batchUpdate(dataClass.getSimpleName() + "s", parsedObjects.values(), -1);
          if(mVerboseMode) System.out.println(" update-batch #" + ++batchNr + " of " + totalBatches);
        }
        totalObjectsParsed += parsedObjects.size();
        
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

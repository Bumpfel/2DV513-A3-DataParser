package handlers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.Genre;
import model.GenreTitleRelation;
import model.IMDBData;
import model.Title;

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
      mDB.exec("DELETE FROM titles");
      mDB.exec("DELETE FROM genres");
      mDB.exec("DELETE FROM genretitlerelations");
      System.out.print(mVerboseMode ? "old data cleared from db\n\n" : "");
    }

    parseAll();
  }
 
    
  private void parseAll() {
    if(mVerboseMode) System.out.println("parsing titles...");
    int numParsedTitles = parseFileAndPutInDB("titles.tsv", Title.class, SQLOperation.INSERT);
    if(mVerboseMode) System.out.println(numParsedTitles + " titles parsed");
    if(mVerboseMode) System.out.println("parsing ratings...");
    int numParsedRatings = parseFileAndPutInDB("ratings.tsv", Title.class, SQLOperation.UPDATE);
    if(mVerboseMode) System.out.println(numParsedRatings + " ratings parsed");
  }

  private int parseFileAndPutInDB(String path, Class<?> clazz, SQLOperation sqlOperation) {
    Map<String, IMDBData> parsedObjects = new HashMap<>();
    List<IMDBData> genreTitleRelations = new ArrayList<>();
    Set<Genre> genres = new HashSet<>();

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
        
          // discard titleType="short"
          // String titleType = attributes.get("titleType");
          // if(titleType != null && titleType.equalsIgnoreCase("short")) {
          //   attributes.clear();
          //   continue;
          // }

          IMDBData object = (IMDBData) clazz.getDeclaredConstructors()[0].newInstance(attributes); // instantiation through reflection
          parsedObjects.put(object.getId(), object);

          // genres
          if(clazz == Title.class) {
            String genresString = attributes.get("genres");
            if(genresString != null) {
              for(String genreString : genresString.split(",")) {
                Genre genre = new Genre(genres.size(), genreString);
                if(!genres.contains(genre)) {
                  genres.add(genre);
                  mDB.exec("INSERT IGNORE INTO genres (" + Genre.getInsertCols() + ") VALUES (" + genre.getInsertValuesString() + ")");
                  if(mVerboseMode) System.out.println(" found and inserted new genre " + genre);
                }
                // batchSize++;
                genreTitleRelations.add(new GenreTitleRelation(genre.getId(), attributes.get("tconst")));
              }
            }
          }

          if(batchSize >= mBatchSize || !scanner.hasNextLine()) {
            batchSize = 0;
            if(sqlOperation == SQLOperation.INSERT) {
              mDB.batchInsertion(clazz.getSimpleName() + "s", parsedObjects.values(), -1);
              if(!genreTitleRelations.isEmpty()) {
                mDB.batchInsertion("genretitlerelations", genreTitleRelations, -1);
                // System.out.println("INSERTING " + genreTitleRelations.size() + " relations");
              }
              if(mVerboseMode) System.out.println(" insert-batch #" + ++batchNr + " of " + totalBatches);
            } else if(sqlOperation == SQLOperation.UPDATE) {
              mDB.batchUpdate(clazz.getSimpleName() + "s", parsedObjects.values(), -1);
              if(mVerboseMode) System.out.println(" update-batch #" + ++batchNr + " of " + totalBatches);
            }
            totalObjectsParsed += parsedObjects.size();
            genreTitleRelations.clear();
            parsedObjects.clear();
          }
        }

        // System.out.println("Genres size: " + genres.size());
        // System.out.println(genres);

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

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
import model.IMDBData;
import model.Title;
import model.TitleGenreRelation;
import model.TitleType;

public class BatchWorker {
  private String mDataFolder;
  private String mDatabase;
  private boolean mVerboseMode = false;
  private DBHandler mDB;
  private int mBatchSize;
  private enum SQLOperation { INSERT, UPDATE }

  public BatchWorker(boolean verboseMode, int batchSize, String database, String dataFolder) {
    mVerboseMode = verboseMode;
    
    mDatabase = database;
    mDataFolder = dataFolder;
    mBatchSize = batchSize;
    mDB = new DBHandler();
    mDB.connect(mDatabase);
  }
  
  public void start(boolean truncateData) {
    if(truncateData) {
      if(mVerboseMode) System.out.println("deleting old data...");
      mDB.exec("SET FOREIGN_KEY_CHECKS = 0");
      mDB.exec("TRUNCATE titletypes");
      mDB.exec("TRUNCATE titlegenrerelations");
      mDB.exec("TRUNCATE titles");
      mDB.exec("TRUNCATE genres");
      mDB.exec("SET FOREIGN_KEY_CHECKS = 1");
      if(mVerboseMode) System.out.println("old data cleared from db\n");
    }

    parseTitlesAndGenres();
    parseRatings();
  }
     
  private void parseTitlesAndGenres() {
    if(mVerboseMode) System.out.println("parsing titles...");
    int numParsedTitles = parseFileAndPutInDB("titles.tsv", SQLOperation.INSERT);
    if(mVerboseMode) System.out.println(numParsedTitles + " titles parsed");
  }
  
  private void parseRatings() {
    if(mVerboseMode) System.out.println("parsing ratings...");
    int numParsedRatings = parseFileAndPutInDB("ratings.tsv", SQLOperation.UPDATE);
    if(mVerboseMode) System.out.println(numParsedRatings + " ratings parsed");
  }

  private int parseFileAndPutInDB(String path, SQLOperation sqlOperation) {
    Map<String, IMDBData> parsedObjects = new HashMap<>();
    List<IMDBData> titleGenreRelations = new ArrayList<>();
    Map<String, Genre> genres = new HashMap<>();
    Map<String, TitleType> titleTypes = new HashMap<>();

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
        
          // titleTypes
          String titleTypeName = attributes.get("titleType");
          if(titleTypeName != null) {
            // discard titleType="tvEpisode"
            if(titleTypeName.equalsIgnoreCase("tvEpisode")) {
              attributes.clear();
              continue;
            }
            
            // add titleType when new are found
            if(!titleTypes.containsKey(titleTypeName)) {
              TitleType newTitleType = new TitleType(titleTypes.size(), titleTypeName);
              titleTypes.put(titleTypeName, newTitleType);
              mDB.insert(newTitleType);
              if(mVerboseMode) System.out.println(" found and inserted new titleType " + titleTypeName);
            }
          }

          IMDBData object = (IMDBData) new Title(attributes);
          // set typeId
          TitleType titleType = titleTypes.get(attributes.get("titleType"));
          if (titleType != null) {
            ((Title) object).setTitleTypeId(titleType.getId());
          }
          parsedObjects.put(object.getId(), object);

          // genres and relations
          String genresString = attributes.get("genres");
          if(genresString != null) {
            for(String genreString : genresString.split(",")) {
              if(!genres.containsKey(genreString)) {
                Genre newGenre = new Genre(genres.size(), genreString);
                genres.put(genreString, newGenre);
                mDB.insert(newGenre);
                // mDB.exec("INSERT IGNORE INTO genres (" + Genre.getInsertCols() + ") VALUES (" + genre.getInsertValuesString() + ")");
                if(mVerboseMode) System.out.println(" found and inserted new genre " + newGenre);
              }
              // batchSize++;
              titleGenreRelations.add(new TitleGenreRelation(genres.get(genreString).getId(), attributes.get("tconst"))); // TODO genre.getId() wrong!!!!!!!!!
            }
          }

          if(batchSize >= mBatchSize || !scanner.hasNextLine()) {
            batchSize = 0;
            if(sqlOperation == SQLOperation.INSERT) {
              mDB.batchInsertion("titles", parsedObjects.values());
              if(!titleGenreRelations.isEmpty()) {
                mDB.batchInsertion("titlegenrerelations", titleGenreRelations);
              }
              if(mVerboseMode) System.out.println(" insert-batch #" + ++batchNr); // + " of " + totalBatches);
            } else if(sqlOperation == SQLOperation.UPDATE) {
              mDB.batchUpdate("titles", parsedObjects.values());
              if(mVerboseMode) System.out.println(" update-batch #" + ++batchNr + " of " + totalBatches);
            }
            totalObjectsParsed += parsedObjects.size();
            titleGenreRelations.clear();
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
  
  
  // TODO batches due to titleType discarding batches can be of unequal sizes
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

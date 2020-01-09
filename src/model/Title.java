package model;

import java.util.Map;

public class Title implements IMDBData {
  private String id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres;
  public String averageRating, numVotes;

  public String getId() { return id; }

  public Title(Map<String, String> map) {
    id = map.get("tconst");
    titleType = map.get("titleType");
    primaryTitle = map.get("primaryTitle");
    originalTitle = map.get("originalTitle");
    isAdult = map.get("isAdult");
    startYear = map.get("startYear");
    endYear = map.get("endYear");
    runtimeMinutes = map.get("runtimeMinutes");
    genres = map.get("genres");
    averageRating = map.get("averageRating");
    numVotes = map.get("numVotes");
 }
  public static String getInsertCols() {
    return "id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres, averageRating, numVotes";
  }
  public String[] getInsertValues() {
    return new String[] { id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres, averageRating, numVotes };
  }
  
  public static String getSQLUpdateString() {
    return "averageRating = ?, numVotes = ?";
  }

  public String[] getUpdateValues() {
    return new String[] { averageRating, numVotes, id };
  }

  @Override
  public String toString() {
    // return primaryTitle;
    return id + " " + primaryTitle + " " + averageRating + " (" + numVotes + " votes)";
  }
}
package model;

import java.util.Map;

public class Title implements IMDBData {
  private String id, titleTypeId, tempTitleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes;
  public String averageRating, numVotes;

  public String getId() { return id; }

  public Title(Map<String, String> map) {
    id = map.get("tconst");
    tempTitleType = map.get("titleType");
    primaryTitle = map.get("primaryTitle");
    originalTitle = map.get("originalTitle");
    isAdult = map.get("isAdult");
    startYear = map.get("startYear");
    endYear = map.get("endYear");
    runtimeMinutes = map.get("runtimeMinutes");
    averageRating = map.get("averageRating");
    numVotes = map.get("numVotes");
 }
  public static String getInsertCols() {
    return "id, titleTypeId, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, averageRating, numVotes";
  }
  public String[] getInsertValues() {
    return new String[] { id, titleTypeId, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, averageRating, numVotes };
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

  public void setTitleTypeId(String id) {
    titleTypeId = id;
  }

  @Override
  public String getInsertValuesString() {
    // TODO Auto-generated method stub
    return null;
  }
}
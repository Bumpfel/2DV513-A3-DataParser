package model;

import java.util.Map;

public class Title implements IMDBData {
  public String id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres, rating, nrvotes;

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
  }

  // not the best solution but should work
  // public Title(String[] data) throws Exception {
  //   if (data.length != Title.class.getDeclaredFields().length - 2) {
  //     throw new Exception("Input data does not match class attributes");
  //   } else {
  //     this.id = data[0];
  //     this.titleType = data[1];
  //     this.primaryTitle = data[2];
  //     this.originalTitle = data[3];
  //     this.isAdult = data[4];
  //     this.startYear = data[5];
  //     this.endYear = data[6];
  //     this.runtimeMinutes = data[7];
  //     this.genres = data[8];
  //   }
  // }


  // not used
  public Title(String id, String titleType, String  primaryTitle, String originalTitle, String isAdult, String startYear, String endYear, String runtimeMinutes, String genres) { //, String rating, String nrvotes) {
    this.id = id;
    this.titleType = titleType;
    this.primaryTitle = primaryTitle;
    this.originalTitle = originalTitle;
    this.isAdult = isAdult;
    this.startYear = startYear;
    this.endYear = endYear;
    this.runtimeMinutes = runtimeMinutes;
    this.genres = genres;
    // this.rating = rating;
    // this.nrvotes = nrvotes;
  }

  public String getInsertCols() {
    return "id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres";
  } 

  public String[] getInsertValues() {
    return new String[] { id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres, rating, nrvotes };
  }

  @Override
  public String toString() {
    return primaryTitle;
  }
}
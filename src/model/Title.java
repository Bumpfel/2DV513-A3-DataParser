package model;

import java.lang.reflect.Field;
import java.util.Map;

public class Title {
  public String id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres, rating, nrvotes;

  // public Title(Map<String, String> map) {
  //   Title.class.getDeclaredField(map)
  // }

  public String getInsertCols() {
    return "id, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres";
  } 

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
    this.rating = rating;
    this.nrvotes = nrvotes;
  }

  @Override
  public String toString() {
    return primaryTitle;
  }
}
package model;

public interface IMDBData {

  public String getId();
  // for db batch insertion. getInserCols and getInsertValues must be ordered in the same way
  public static String getInsertCols(){ return null; }
  public String[] getInsertValues();

}
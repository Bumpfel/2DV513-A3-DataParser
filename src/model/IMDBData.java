package model;

public interface IMDBData {

  public String getId();
  // for db batch insertion. getInserCols and getInsertValues must be ordered in the same way
  // public String getInsertCols();
  // public String getUpdateCols(); 
  public String[] getInsertValues();
  public String[] getUpdateValues(); 

}
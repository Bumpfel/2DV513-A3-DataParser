package model;

public class GenreTitleRelation implements IMDBData {
  private String genreId, titleId;

  public String getId() { return null; }

  public GenreTitleRelation(String _genreId, String _titleId) {
    genreId = _genreId;
    titleId = _titleId;
  }

  public static String getInsertCols() {
    return "genreId, titleId";
  }
  public String[] getInsertValues() {
    return new String[] { genreId, titleId };
  }

  public String[] getUpdateValues() {
    // not implemented
    return null;
  }

}
package model;

public class TitleGenreRelation implements IMDBData {
  private String genreId, titleId;

  public String getId() { return null; }

  public TitleGenreRelation(String _genreId, String _titleId) {
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

  @Override
  public String getInsertValuesString() {
    // TODO Auto-generated method stub
    return null;
  }

}
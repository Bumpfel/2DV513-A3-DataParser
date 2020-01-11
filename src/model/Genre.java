package model;

public class Genre implements IMDBData {
  private String id, genreName;

  public String getId() { return id; }

  public Genre(int _id, String name) {
    id = "" + _id;
    genreName = name;
  }

  public static String getInsertCols() {
    return "id, genreName";
  }
  public String[] getInsertValues() {
    return new String[] { id, genreName };
  }
  public String getInsertValuesString() {
    return "'" + id + "', '" + genreName + "'";
  }

  public String[] getUpdateValues() {
    // not implemented
    return null;
  }

  @Override
  public int hashCode() {
    return genreName.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Genre) {
      Genre other = (Genre) obj;
      return genreName.equalsIgnoreCase(other.genreName);
    }
    return false;
  }


  @Override
  public String toString() {
    return id + ": " + genreName;
  }


}
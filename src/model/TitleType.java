package model;

public class TitleType implements IMDBData {
  private String id, typeName;

  public String getId() { return id; }

  public TitleType(int _id, String name) {
    id = "" + _id;
    typeName = name;
  }

  public static String getInsertCols() {
    return "id, typeName";
  }
  public String[] getInsertValues() {
    return new String[] { id, typeName };
  }
  public String getInsertValuesString() {
    return "'" + id + "', '" + typeName + "'";
  }

  public String[] getUpdateValues() {
    // not implemented
    return null;
  }

  @Override
  public int hashCode() {
    return typeName.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj instanceof Genre) {
      TitleType other = (TitleType) obj;
      return typeName.equalsIgnoreCase(other.typeName);
    }
    return false;
  }


  @Override
  public String toString() {
    return id + ": " + typeName;
  }


}
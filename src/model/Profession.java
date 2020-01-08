package model;

public class Profession implements IMDBData {
  private static int nextId = 0;
  private String id;
  private String professionName;

  public String getId() { return id; }

  public Profession(String _professionName) {
    id = "" + (nextId ++);
    professionName = _professionName;
  }

  public String getInsertCols() {
    return "id, professionName";
  } 
  public String[] getInsertValues() {
    return new String[] { id, professionName };
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Profession) {
      Profession other = (Profession) o;
      return professionName.equals(other.professionName);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return professionName.hashCode();
  }

  @Override
  public String toString() {
    return id + " " + professionName;
  }
}
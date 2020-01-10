package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Name implements IMDBData {
  private String id, primaryName, birthYear, deathYear, tempPrimaryProfession, knownForTitles;

  public String getId() { return id; }

  public Name(Map<String, String> map) {
    id = map.get("nconst");
    primaryName = map.get("primaryName");
    birthYear = map.get("birthYear");
    deathYear = map.get("deathYear");
    tempPrimaryProfession = map.get("primaryProfession");
    knownForTitles = map.get("knownForTitles");
  }

  public static String getInsertCols() {
    return "id, primaryName, birthYear, deathYear, primaryProfession, knownForTitles";
  }
  public String[] getInsertValues() {
    return new String[] { id, primaryName, birthYear, deathYear, tempPrimaryProfession, knownForTitles };
  }

  public String[] getUpdateValues() {
    // not implemented
    return null;
  }

  @Override
  public String toString() {
    return primaryName;
  }
}
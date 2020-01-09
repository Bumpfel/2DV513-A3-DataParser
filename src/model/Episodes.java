package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Episodes implements IMDBData {
  private String id, parentId, seasonNumber, episodeNumber;

  public String getId() { return id; }

  public Episodes(Map<String, String> map) {
    id = map.get("nconst");
    parentId = map.get("parentId");
    seasonNumber = map.get("seasonNumber");
    episodeNumber = map.get("episodeNumber");
  }

  public static String getInsertCols() {
    return "id, parentId, seasonNumber, episodeNumber";
  }
  public String[] getInsertValues() {
    return new String[] { id, parentId, seasonNumber, episodeNumber };
  }

  public String[] getUpdateValues() {
    // not implemented
    return null;
  }
}
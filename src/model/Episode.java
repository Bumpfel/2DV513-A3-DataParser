package model;

import java.util.Map;

public class Episode implements IMDBData {
  private String id, parentId, seasonNumber, episodeNumber;

  public String getId() { return id; }

  public Episode(Map<String, String> map) {
    id = map.get("tconst");
    parentId = map.get("parentTconst");
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
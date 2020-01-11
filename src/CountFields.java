import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class CountFields {
  public static void main(String[] args) {
    Map<String, Integer> allGenres = new HashMap<>();

    int numTvEpisodes = 0;
    int numTitles = 0;
    File file = new File("data/titles.tsv");
    try (Scanner scanner = new Scanner(file)) {
      String[] fields = scanner.nextLine().split("\t");
      // System.out.println("Found fields: " + Arrays.toString(fields));
      Map<String, String> attributes = new HashMap<>();

      while (scanner.hasNextLine()) {
        String[] data = scanner.nextLine().split("\t");

        // map fields with data
        for (int i = 0; i < fields.length; i++) {
          attributes.put(fields[i], data[i]);
        }

        if(attributes.get("titleType").equalsIgnoreCase("tvEpisode")) {
          numTvEpisodes ++;
          continue;
        }
        numTitles++;

        String genresString = attributes.get("genres");
        for(String genre : genresString.split(",")) {
          int number = allGenres.get(genre) != null ? allGenres.get(genre) : 1;
          allGenres.put(genre, number + 1);
        }
      }
      System.out.println("Total number of titles (non tv-episode): " + numTitles);
      System.out.println("Total number of tv-episodes: " + numTvEpisodes);


    } catch (Exception e) {
      e.printStackTrace();
      System.exit(-1);
    }

    // done parsing
    int sum = 0;
    int skippedGenres = 0;
    for(String genre : allGenres.keySet()) {
      System.out.println(genre + ": " + allGenres.get(genre));
      if(!genre.equalsIgnoreCase("\\N")) {
        sum += allGenres.get(genre);
      } else {
        skippedGenres += allGenres.get(genre);
      }
    }
    System.out.println("==================================");
    System.out.println("Expected # of genreTitleRelations: " + sum);
    System.out.println("Skipped: " + skippedGenres);
  }

}
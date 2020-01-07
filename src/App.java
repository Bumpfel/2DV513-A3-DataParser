import java.text.DecimalFormat;
import java.util.Set;

import handlers.DataParser;
import model.IMDBData;
import model.Title;

class App {
  public static void main(String[] args)  throws Exception {
    Set<IMDBData> titles = DataParser.parseTitles();
    // System.out.println("parsed");
    // Set<IMDBData> persons = DataParser.parseNames();
    // Set<IMDBData> professions = DataParser.parseProfessions();


    int largestFound = 0;
    String largestTitle =  null;
    int adultMovies = 0;
    for(IMDBData data : titles) {
      Title title = (Title) data;
      adultMovies += Integer.parseInt(title.isAdult);
      if(title.originalTitle.length() > largestFound) {
        // largestFound = title.originalTitle.length();
        // largestTitle = title.originalTitle;
      }
    }
    // System.out.println("largestFound: " + largestFound);


    DecimalFormat df = new DecimalFormat("#.##");

    System.out.println("Total nr of titles: " + titles.size());
    System.out.println("Nr of adult movies: " + adultMovies + " (" + df.format((double) adultMovies /  titles.size() * 100) + "%)");

    // for(IMDBData title : titles) {
    //   System.out.println(title);
    // }
    // System.out.println(titles.iterator().next().getClass());
   
  }

}
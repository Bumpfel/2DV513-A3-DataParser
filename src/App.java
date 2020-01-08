import java.util.ArrayList;
import java.util.Collection;

import handlers.DBHandler;
import handlers.DataParser;
import model.IMDBData;

class App {
  public static void main(String[] args) {
    DataParser parser = new DataParser();
    DBHandler db = new DBHandler();
    db.connect("2dv513a3");    

    // clear old data
    db.exec("DELETE FROM titles");
    db.exec("DELETE FROM names");
    db.exec("DELETE FROM professions");
    System.out.println("old data cleared from db");

    System.out.println("Starting data parsing...");

    // Titles
    Collection<IMDBData> titles = parser.parseTitles();
    System.out.println(" titles parsed");
    db.batchInsertion("titles", titles);
    System.out.println(" titles inserted into db");

    Collection<IMDBData> names = new ArrayList<IMDBData>();
    Collection<IMDBData> professions = new ArrayList<IMDBData>();
    parser.parseNamesAndProfessions(names, professions);
    System.out.println(" names and professions parsed");
    db.batchInsertion("names", names);
    System.out.println(" names inserted into db");

    // db.batchInsertion("professions", professions);
    // System.out.println("-professions inserted into db");

    System.out.println("All data inserted into db");
  }

}
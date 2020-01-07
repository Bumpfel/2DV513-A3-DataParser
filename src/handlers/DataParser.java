package handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.IMDBData;
import model.Title;

public class DataParser {
    private static String dataPath = "testData/";

    public static Set<IMDBData> parseTitles() throws Exception {
        Set<IMDBData> titles = parseFile("titles.tsv");
        // Set<IMDBData> ratings = parseFile("ratings.tsv");

        // TODO kombinera ovanstående eftersom de ska vara i samma tabell

        return titles;
    }
    
    public static Set<IMDBData> parseNames() throws Exception {
        Set<IMDBData> names = parseFile("names.tsv");
        return names;
    }
    
    
    private static Set<IMDBData> parseFile(String path) throws Exception {
        Set<IMDBData> data = new HashSet<>();

        File file = new File(dataPath + path);
        try(Scanner scanner = new Scanner(file)) {
            String[] fields = scanner.nextLine().split("\t");
            // System.out.println("Found fields: " + Arrays.toString(fields));

            Map<String, String> smt = new HashMap<>();
            
            while(scanner.hasNextLine()) {
                String[] titleData = scanner.nextLine().split("\t");
                
                for(int i = 0; i < fields.length; i ++) {
                    smt.put(fields[i], titleData[i]);
                }
                data.add(new Title(smt));
                smt.clear();
            }
        }
        return data; 
    }


}
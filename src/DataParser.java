import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.Title;

public class DataParser {
    private static String dataPath = "demoData/";
    public static void main(String[] args) throws Exception {
        parseFile("title.tsv");
    }

    
    
    static Set<?> parseFile (String filePath) throws FileNotFoundException {
        Set<?> data = new HashSet<>();

        File file = new File(dataPath + filePath);
        try(Scanner scanner = new Scanner(file)) {
            String[] fields = scanner.nextLine().split("\t");

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] titleData = line.split("\t");
                // Map<String, String> map  = new HashMap<>();
                // for(int i = 0; i < fields.length; i++) {
                //     map.put(fields[i], titleData[i]);
                // }
                // Title title = new Title(map);
                Title title = new Title(titleData[0], titleData[1], titleData[2], titleData[3], titleData[4], titleData[5], titleData[6], titleData[7], titleData[8]);
                System.out.println(title);
            }
        }
        return data; 
    }
}
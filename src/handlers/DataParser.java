package handlers;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import model.IMDBData;
import model.Title;
import model.Name;
import model.Profession;

public class DataParser {
    private String mDataPath = "data/";

    public Collection<IMDBData> parseTitles() {
        Map<String, IMDBData> titles = parseFile("titles.tsv", Title.class);
        Map<String, IMDBData> ratings = parseFile("ratings.tsv", Title.class);

        // merge ratings with titles
        for(IMDBData data : titles.values()) {
            Title title = (Title) data;
            Title dat = (Title) ratings.get(title.getId());

            if(dat != null) {
                title.averageRating = dat.averageRating;
                title.numVotes = dat.numVotes;
            }
        }

        return titles.values();
    }
    
    public void parseNamesAndProfessions(Collection<IMDBData> names, Collection<IMDBData> professions) {
        System.out.println("parsing names");
        names.addAll(parseFile("names.tsv", Name.class).values());

        System.out.println("names parsed");

        // Map professions to separate collection, swap professions in names for profession id's
        
        // Map<String, Profession> allProfessions = new HashMap<>();
        // for(IMDBData data : names) {
        //     Name name = (Name) data;
        //     Profession newProfession = null;
        //     for(String professionName : name.getPrimaryProfession().split(",")) {
        //         if(!allProfessions.containsKey(professionName)) {
        //             newProfession = new Profession(professionName);
        //             allProfessions.put(professionName, newProfession);
        //         }
        //         name.addPrimaryProfession(allProfessions.get(professionName).getId());
        //     }
        // }
        // professions.addAll(allProfessions.values());
    }
    
    private Map<String, IMDBData> parseBatch(String path, Class<?> dataClass) {
        Map<String, IMDBData> parsedObjects = new HashMap<>();
        try {
            File file = new File(mDataPath + path);
            try(Scanner scanner = new Scanner(file)) {

                // record fields
                String[] fields = scanner.nextLine().split("\t");
                // System.out.println("Found fields: " + Arrays.toString(fields));
                Map<String, String> attributes = new HashMap<>();
                
                int bytesScanned = 0, temp = 0;
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] data = line.split("\t");
                    bytesScanned += line.length();
                    temp += line.length();
                    // map fields with data to allow safer object creation (order does not matter in constructor)
                    for(int i = 0; i < fields.length; i ++) {
                        attributes.put(fields[i], data[i]);
                    }

                    IMDBData object = null;
                    if(dataClass == Title.class) {
                        object = new Title(attributes);
                    } else if(dataClass == Name.class) {
                        if(temp > 100 * Math.pow(1024, 2)) {
                            temp = 0;
                            DecimalFormat df = new DecimalFormat("#.#");
                            double percent = (double) bytesScanned / file.length() * 100;
                            System.out.println("Bytes scanned: " + (int) (bytesScanned / Math.pow(1024, 2)) + " MB of " + (int) (file.length() / Math.pow(1024, 2)) + " MB (" + df.format(percent) + "%)");
                        }
                        object = new Name(attributes);
                    } else {
                        continue;
                    }
                    parsedObjects.put(object.getId(), object);
                    attributes.clear();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return parsedObjects;
    }


}
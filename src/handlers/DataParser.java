package handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.IMDBData;
import model.Title;
import model.Name;

public class DataParser {
    private String mDataPath = "data/";
    private boolean mDebug = false;

    public DataParser(boolean debug) {
        mDebug = debug;
    }

    public Collection<IMDBData> parseTitles() {
        Map<String, IMDBData> titles = parseFile("titles.tsv", Title.class);
        System.out.print(mDebug ? " titles parsed\n" : "");
        Map<String, IMDBData> ratings = parseFile("ratings.tsv", Title.class);
        System.out.print(mDebug ? " ratings parsed\n" : "");

        // merge ratings with titles
        for(IMDBData data : titles.values()) {
            Title title = (Title) data;
            Title dat = (Title) ratings.get(title.getId());

            if(dat != null) {
                title.averageRating = dat.averageRating;
                title.numVotes = dat.numVotes;
            }
        }
        System.out.print(mDebug ? " titles and ratings merged\n" : "");

        return titles.values();
    }
    
    // public void parseNamesAndProfessions(Collection<IMDBData> names, Collection<IMDBData> professions) {
    public Collection<IMDBData> parseNames() {
        System.out.print(mDebug ? " parsing names\n" :"");
        return parseFile("names.tsv", Name.class).values();

        // System.out.print(mDebug ? " names parsed" : "");

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


    private Map<String, IMDBData> parseFile(String path, Class<?> dataClass) {
        Map<String, IMDBData> parsedObjects = new HashMap<>();
        try {
            File file = new File(mDataPath + path);
            try(Scanner scanner = new Scanner(file)) {

                // record fields
                String[] fields = scanner.nextLine().split("\t");
                // System.out.println("Found fields: " + Arrays.toString(fields));
                Map<String, String> attributes = new HashMap<>();
                
                // int bytesScanned = 0, temp = 0;
                while(scanner.hasNextLine()) {
                    // String line = 
                    String[] data = scanner.nextLine().split("\t");
                    // bytesScanned += line.length();
                    // temp += line.length();
                    // map fields with data to allow safer object creation (order does not matter in constructor)
                    for(int i = 0; i < fields.length; i ++) {
                        attributes.put(fields[i], data[i]);
                    }

                    IMDBData object = null;
                    if(dataClass == Title.class) {
                        object = new Title(attributes);
                    } else if(dataClass == Name.class) {
                        // if(temp > 100 * Math.pow(1024, 2)) {
                        //     temp = 0;
                        //     DecimalFormat df = new DecimalFormat("#.#");
                        //     double percent = (double) bytesScanned / file.length() * 100;
                        //     System.out.println("Bytes scanned: " + (int) (bytesScanned / Math.pow(1024, 2)) + " MB of " + (int) (file.length() / Math.pow(1024, 2)) + " MB (" + df.format(percent) + "%)");
                        // }
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






    // batch parsing. not finished
    
    private Scanner mScanner;
    private int mBatchSize;

    public DataParser(String filePath, int batchSize) {
        mBatchSize = batchSize;
        File file = new File(mDataPath + filePath);
        try {
            mScanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private Map<String, IMDBData> parseNextBatch(String path, Class<?> dataClass) {
        Map<String, IMDBData> parsedObjects = new HashMap<>();
        try {
            // record fields
            String[] fields = mScanner.nextLine().split("\t");
            // System.out.println("Found fields: " + Arrays.toString(fields));
            Map<String, String> attributes = new HashMap<>();
            
            // int bytesScanned = 0, temp = 0;
            while(parsedObjects.size() > mBatchSize && mScanner.hasNextLine()) {
                String line = mScanner.nextLine();
                String[] data = line.split("\t");
                // bytesScanned += line.length();
                // temp += line.length();
                // map fields with data to allow safer object creation (order does not matter in constructor)
                for(int i = 0; i < fields.length; i ++) {
                    attributes.put(fields[i], data[i]);
                }

                IMDBData object = null;
                if(dataClass == Title.class) {
                    object = new Title(attributes);
                } else if(dataClass == Name.class) {
                    // if(temp > 100 * Math.pow(1024, 2)) {
                    //     temp = 0;
                    //     DecimalFormat df = new DecimalFormat("#.#");
                    //     double percent = (double) bytesScanned / file.length() * 100;
                    //     System.out.println("Bytes scanned: " + (int) (bytesScanned / Math.pow(1024, 2)) + " MB of " + (int) (file.length() / Math.pow(1024, 2)) + " MB (" + df.format(percent) + "%)");
                    // }
                    object = new Name(attributes);
                } else {
                    continue;
                }
                parsedObjects.put(object.getId(), object);
                attributes.clear();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return parsedObjects;
    }

    public boolean hasNextBatch() {
        return mScanner.hasNextLine();
    }

}
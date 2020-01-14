For parsing and inserting imdb datasets into sql db

# Running instructions

## Prepare the DB
1. Create an empty database
2. Import the structure file sql/db_structure.sql
3. Adjust connection settings in handlers/DBHandler.java if needed

## Prepare the data sets
* Use the test data. Change dataFolder to "testData" in App.java

OR

* Use the real data
1. Download "title.basics.tsv.gz", and "title.ratings.tsv.gz" from https://datasets.imdbws.com/
2. Unzip the data sets to a folder called "data"
3. Name the files "titles.tsv", "ratings.tsv"

## Run the app
1. Change settings in App.java if needed
  * verboseMode gives you more detailed information during the parsing/db insertion
  * batchSize - how much data the app should parse and insert at a time. Lower if needed for memory
  * database - the name of your database
  * dataFolder - the name of the folder where you keep your data sets
2. Run App.java

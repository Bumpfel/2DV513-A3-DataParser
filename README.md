# 2DV513-A3-DataParser
For parsing and inserting imdb datasets into sql db

# Instructions #

# Prepare the DB
1. Create an empty database called "2dv513a3"
2. Import the structure file sql/db_structure.sql
3. Adjust connection settings in handlers/DBHandler.java if needed (probably don't need with MAMP/WAMP installed)

# Prepare the data sets
* Use the test data. Change dataFolder to "testData" in App.java

OR

* Use the real data
1. Download "name.basics.tsv.gz", "title.basics.tsv.gz", "title.ratings.tsv.gz", and "title.episode.tsv.gz" from https://datasets.mdbws.com/
2. Unzip the data sets to a folder called "data"
3. Name the files "titles.tsv", "ratings.tsv", "names.tsv", and "episodes.tsv"

# Run the app
1. Change to verbose mode true in App.java if you want more detailed information during the parsing
2. Adjust batchSize if needed for memory
3. Run App.java

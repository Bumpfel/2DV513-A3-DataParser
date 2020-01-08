# 2DV513-A3-DataParser
For parsing and inserting imdb datasets into sql db

# Instructions #

# Prepare the DB
1. Create an empty database called "2dv513a3"
2. Import the structure file sql/db_structure.sql
3. Adjust connection settings in handlers/DBHandler.java if needed (probably don't need with MAMP/WAMP installed)

# Prepare the data sets
* Use the test data. Nothing needs to be changed. It's all there

OR

* Use the real data
1. Download "name.basics.tsv.gz", "title.basics.tsv.gz", and "title.ratings.tsv.gz" from https://datasets.imdbws.com/
2. Unzip the data sets to a folder and change mDataPath in DataParser accordingly
3. Name the files "titles.tsv", "ratings.tsv", and "names.tsv"

# Run the app
1. Run App.java

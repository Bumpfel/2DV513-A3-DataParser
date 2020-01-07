package handlers;

import java.sql.*;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import model.IMDBData;

public class DBHandler {
  private Connection conn;

  public void connect(String database) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?rewriteBatchedStatements=true", "root", ""); // running on localhost only, so root user is fine
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public void batchInsertion(String table, Iterable<IMDBData> data) {
    if(!data.iterator().hasNext()) {
      return;
    }

    try {
      // builds a comma-separated string "?, ?, ?, ..." with a parameter(?) for each column
      // https://stackoverflow.com/questions/1812891/java-escape-string-to-prevent-sql-injection
      
      String colsString = data.iterator().next().getInsertCols();
      
      StringBuilder parameterBuilder = new StringBuilder();
      for(int i = 0; i < colsString.split(",").length; i++) {
        parameterBuilder.append("?, ");
      }
      String parameterString = parameterBuilder.substring(0, parameterBuilder.length() - 2);
      
      PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO " + table + "(" + colsString + ") VALUES (" + parameterString + ")");
      // long timestamp = System.currentTimeMillis();

      for(IMDBData dataObject : data) {
        String[] insertValues = dataObject.getInsertValues();
        for(int i = 1; i <= insertValues.length; i++) {
          // replaces each parameter(?) with data values
          stmt.setString(i, insertValues[i - 1]);
        }
        stmt.addBatch();
      }
      // execute batch and count insertions
      stmt.executeBatch();
      // attemptedInsertions += stmt.executeBatch().length;

      // done
      // totalTimeTaken += System.currentTimeMillis() - timestamp;
    } catch(MySQLIntegrityConstraintViolationException | BatchUpdateException e) {
      // duplicate entry
      System.err.println("Error inserting into " + table + ". " + e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}

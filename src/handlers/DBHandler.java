package handlers;

import java.sql.*;
import java.util.Collection;

import com.mysql.jdbc.MysqlDataTruncation;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

import model.GenreTitleRelation;
import model.IMDBData;
import model.Title;

public class DBHandler {
  private Connection mConn;
  
  public DBHandler() {
  }

  public void connect(String database) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      mConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database + "?rewriteBatchedStatements=true", "root", ""); // running on localhost only, so root user is fine
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public boolean exec(String query) {
    try {
      Statement stmt = mConn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void batchInsertion(String table, Collection<IMDBData> data) {
    if(!data.iterator().hasNext()) {
      return;
    }
    
    Class<?> dataClass = data.iterator().next().getClass();
    String colsString = null;
    if(dataClass == Title.class) {
      colsString = Title.getInsertCols();
    } else if(dataClass == GenreTitleRelation.class) {
      colsString = GenreTitleRelation.getInsertCols();
    }
   
    // builds a comma-separated string "?, ?, ?, ..." with a parameter(?) for each column     
    StringBuilder parameterBuilder = new StringBuilder();
    for(int i = 0; i < colsString.split(",").length; i++) {
      parameterBuilder.append("?, ");
    }
    String parameterString = parameterBuilder.substring(0, parameterBuilder.length() - 2);
    
    try {
      PreparedStatement stmt = mConn.prepareStatement("INSERT IGNORE INTO " + table + "(" + colsString + ") VALUES (" + parameterString + ")");

      for(IMDBData dataObject : data) {
        String[] insertValues = dataObject.getInsertValues();
        for(int i = 1; i <= insertValues.length; i++) {
          // replaces each parameter(?) with data values
          stmt.setString(i, insertValues[i - 1]);
        }
        stmt.addBatch();
      }
      stmt.executeBatch();
      
    } catch(MysqlDataTruncation e) {
      e.printStackTrace();
    } catch(MySQLIntegrityConstraintViolationException | BatchUpdateException e) {
      e.printStackTrace();
      // System.err.println("Error inserting into " + table + ". " + e.getMessage());
      System.exit(-1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void batchUpdate(String table, Collection<IMDBData> data) {
    if(!data.iterator().hasNext()) {
      return;
    }

    String updateString = null;
    Class<?> dataClass = data.iterator().next().getClass();
    if(dataClass == Title.class) {
      updateString = Title.getSQLUpdateString();
    }
    
    try {
      PreparedStatement stmt = mConn.prepareStatement("UPDATE " + table + " SET " + updateString + " WHERE id = ?");

      for(IMDBData dataObject : data) {
        String[] updateValues = dataObject.getUpdateValues();
        for(int i = 1; i <= updateValues.length; i++) {
          // replaces each parameter(?) with data values
          stmt.setString(i, updateValues[i - 1]);
        }
        stmt.addBatch();
      }
      stmt.executeBatch();

    } catch(MySQLIntegrityConstraintViolationException | BatchUpdateException e) {
      System.err.println("Error updating " + table + ". " + e.getMessage());
      System.exit(-1);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}

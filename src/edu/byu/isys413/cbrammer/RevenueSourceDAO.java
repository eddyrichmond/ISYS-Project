/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class RevenueSourceDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static RevenueSourceDAO instance = null;
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private RevenueSourceDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized RevenueSourceDAO getInstance() {
    if (instance == null) {
      instance = new RevenueSourceDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public RevenueSource create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      RevenueSource pkg = new RevenueSource(id);
      pkg.setObjectAlreadyInDB(false);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public RevenueSource read(String id) throws DataException {
    RevenueSource pkg;
   
    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read RevenueSource", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized RevenueSource read(String id, Connection conn) throws Exception {
      RevenueSource pkg = new RevenueSource(id);


    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM revenueSource WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setTransLineGuid(rs.getString(2));
            pkg.setType(rs.getString(3));
            pkg.setCommGuid(rs.getString(4));
      } else {
          throw new DataException("bad RevenueSource read");
      }
      if(pkg.getTransLineGuid() != null && !pkg.getTransLineGuid().equals("")){
          TransLine line = TransLineDAO.getInstance().read(pkg.getTransLineGuid(), conn);
          pkg.setTransLine(line);
      }

      if(pkg.getCommGuid() != null && !pkg.getCommGuid().equals("")){
          Commission comm = CommissionDAO.getInstance().read(pkg.getCommGuid(), conn);
          pkg.setComm(comm);
      }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache

      

    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(RevenueSource pkg) throws DataException {

      // get pkg jdbc connection
      Connection conn = cp.get();

      try {
          save(pkg, conn);
          // commit
          conn.commit();
      } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DataException("can't roll back", e);
            }
            throw new DataException("Problem saving RevenueSource", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(RevenueSource pkg, Connection conn) throws Exception {

      if(pkg.getComm() != null)
            CommissionDAO.getInstance().save(pkg.getComm(), conn);

    // if not dirty, return
      if(!pkg.isDirty() && pkg.isObjectAlreadyInDB()) {
          return;
      }

    // call either update() or insert()
        if(pkg.isObjectAlreadyInDB()) {
            update(pkg, conn);
        } else {
            insert(pkg, conn);
        }
  }//save

   /** Saves an existing pkg to the database */
  private void update(RevenueSource pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE revenueSource SET transLineGuid=?, type=?, commGuid=? WHERE guid LIKE ?");
      pstmt.setString(1, pkg.getTransLineGuid());
      pstmt.setString(2, pkg.getType());
      pstmt.setString(3, pkg.getCommGuid());
      pstmt.setString(4, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad RevenueSource update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(RevenueSource pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO revenueSource VALUES (?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getTransLineGuid());
      pstmt.setString(3, pkg.getType());
      pstmt.setString(4, pkg.getCommGuid());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad RevenueSource update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(RevenueSource pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<RevenueSource> getAll() throws DataException {
    // get pkg jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use pkg finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

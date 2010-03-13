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
public class TransLineDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static TransLineDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private TransLineDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized TransLineDAO getInstance() {
    if (instance == null) {
      instance = new TransLineDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public TransLine create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      TransLine pkg = new TransLine(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public TransLine read(String id) throws DataException {
    // check cache
    TransLine pkg = (TransLine) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read TransLine", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized TransLine read(String id, Connection conn) throws Exception {
    // check cache
      TransLine pkg = (TransLine) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM transLine WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new TransLine(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setTransGuid(rs.getString(2));
      } else {
          throw new DataException("bad transLine read");
      }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache

      cache.put(pkg.getId(), pkg);

    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(TransLine pkg) throws DataException {

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
            throw new DataException("Problem saving TransLine", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(TransLine pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

        if(pkg.getRs() != null)
            pkg.getRs().save(conn);

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
  private void update(TransLine pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE transLine SET transGuid=?, revenuesourceguid=? WHERE guid LIKE ?");
    
      pstmt.setString(1, pkg.getTransGuid());
      pstmt.setString(2, pkg.getRevenueSourceGuid());
      pstmt.setString(3, pkg.getId());
      

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad transLine update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(TransLine pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transLine VALUES (?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getTransGuid());
      pstmt.setString(3, pkg.getRevenueSourceGuid());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad TransLine update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(TransLine pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<TransLine> getAll() throws DataException {
    // get pkg jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use pkg finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

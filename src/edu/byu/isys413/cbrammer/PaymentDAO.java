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
public class PaymentDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static PaymentDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private PaymentDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized PaymentDAO getInstance() {
    if (instance == null) {
      instance = new PaymentDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public Payment create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Payment pkg = new Payment(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Payment read(String id) throws DataException {
    // check cache
    Payment pkg = (Payment) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Payment", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Payment read(String id, Connection conn) throws Exception {
    // check cache
      Payment pkg = (Payment) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM payment WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new Payment(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setAmount(Double.parseDouble(rs.getString(2)));
            pkg.setChange(Double.parseDouble(rs.getString(3)));
            pkg.setType(rs.getString(4));
      } else {
          throw new DataException("bad Payment read");
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
  public void save(Payment pkg) throws DataException {

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
            throw new DataException("Problem saving Payment", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(Payment pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);
     

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
  private void update(Payment pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE payment SET amount=?, change=?, type=? WHERE guid LIKE ?");
      pstmt.setDouble(1, pkg.getAmount());
      pstmt.setDouble(2, pkg.getChange());
      pstmt.setString(3, pkg.getType());
      pstmt.setString(4, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Payment update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(Payment pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO payment VALUES (?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setDouble(2, pkg.getAmount());
      pstmt.setDouble(3, pkg.getChange());
      pstmt.setString(4, pkg.getType());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Payment update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Payment pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Payment> getAll() throws DataException {
    // get pkg jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use pkg finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

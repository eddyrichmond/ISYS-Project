/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Garrett
 */
public class CommissionFactorDAO {

     /////////////////////////////////////////////
  ///   Singleton code

  private static CommissionFactorDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private CommissionFactorDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized CommissionFactorDAO getInstance() {
    if (instance == null) {
      instance = new CommissionFactorDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public CommissionFactor create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      CommissionFactor pkg = new CommissionFactor(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;

  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public CommissionFactor read(String id) throws DataException {
    // check cache
    CommissionFactor pkg = (CommissionFactor) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read CommissionFactor", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized CommissionFactor read(String id, Connection conn) throws Exception {
    // check cache
      CommissionFactor pkg = (CommissionFactor) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM CommissionFactor WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new CommissionFactor(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setRevenueType(rs.getString(2));
            pkg.setFactor(rs.getDouble(3));
      } else {
          throw new DataException("bad CommissionFactor read");
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
  public void save(CommissionFactor pkg) throws DataException {

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
            throw new DataException("Problem saving CommissionFactor", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(CommissionFactor pkg, Connection conn) throws Exception {
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
  private void update(CommissionFactor pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE CommissionFactor SET revenueType=?, factor=? WHERE guid LIKE ?");

      pstmt.setString(1, pkg.getRevenueType());
      pstmt.setDouble(2, pkg.getFactor());
      pstmt.setString(3, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad CommissionFactor update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(CommissionFactor pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO CommissionFactor VALUES (?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getRevenueType());
      pstmt.setDouble(3, pkg.getFactor());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad CommissionFactor update");
      }
      pstmt.close();

  }





  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Accounting pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Accounting> getAll() throws DataException {
    // get pkg jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use pkg finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.




}

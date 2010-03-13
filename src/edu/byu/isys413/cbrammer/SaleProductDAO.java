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
public class SaleProductDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static SaleProductDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private SaleProductDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized SaleProductDAO getInstance() {
    if (instance == null) {
      instance = new SaleProductDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public SaleProduct create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      SaleProduct pkg = new SaleProduct(id);
      pkg.setObjectAlreadyInDB(false);
      pkg.setDirty(true);
    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public SaleProduct read(String id) throws DataException {
    // check cache
    SaleProduct pkg = (SaleProduct) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read SaleProduct", ex);
    } finally {
        cp.release(conn);
    }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized SaleProduct read(String id, Connection conn) throws Exception {
    // check cache
      SaleProduct pkg = (SaleProduct) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM saleProduct WHERE revenueSourceGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new SaleProduct(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setQuantity(Integer.parseInt(rs.getString(2)));
            pkg.setProductGuid(rs.getString(3));
      } else {
          throw new DataException("bad SaleProduct read");
      }

      if(pkg.getProductGuid() != null && !pkg.getProductGuid().equals("")) {
          pkg.setProduct(ProductDAO.getInstance().read(pkg.getProductGuid(), conn));
      }

      RevenueSource rev = RevenueSourceDAO.getInstance().read(id, conn);
      pkg.setTransLineGuid(rev.getTransLineGuid());
      pkg.setTransLine(rev.getTransLine());
      pkg.setType(rev.getType());

    // put in the cache

      cache.put(pkg.getId(), pkg);

    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(SaleProduct pkg) throws DataException {

      // get pkg jdbc connection
      Connection conn = cp.get();

      try {
          pkg.save(conn);
          // commit
          conn.commit();
      } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new DataException("can't roll back", e);
            }
            throw new DataException("Problem saving SaleProduct", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(SaleProduct pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

        Boolean dirtyFlag = pkg.isDirty();
        Boolean inDB = pkg.isObjectAlreadyInDB();
        
        pkg.setDirty(dirtyFlag);
        pkg.setObjectAlreadyInDB(inDB);
        
        
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
  private void update(SaleProduct pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE saleProduct SET quantity=?, productGuid=? WHERE revenueSourceGuid LIKE ?");
    
      pstmt.setInt(1, pkg.getQuantity());
      pstmt.setString(2, pkg.getProductGuid());
      pstmt.setString(3, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad SaleProduct update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(SaleProduct pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO saleProduct VALUES (?,?, ?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setInt(2, pkg.getQuantity());
      pstmt.setString(3, pkg.getProductGuid());
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad SaleProduct update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(SaleProduct pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<SaleProduct> getAll() throws DataException {
    // get pkg jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use pkg finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

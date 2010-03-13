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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class PhysicalProductDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static PhysicalProductDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private PhysicalProductDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized PhysicalProductDAO getInstance() {
    if (instance == null) {
      instance = new PhysicalProductDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public PhysicalProduct create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      PhysicalProduct pkg = new PhysicalProduct(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public PhysicalProduct read(String id) throws DataException {
    // check cache
    PhysicalProduct pkg = (PhysicalProduct) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read PhysicalProduct", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized PhysicalProduct read(String id, Connection conn) throws Exception {
        return read(id, conn, Boolean.TRUE);
  }//read

   /** Internal method to read an existing pkg from the database */
  synchronized PhysicalProduct read(String id, Connection conn, Boolean useCache) throws Exception {
    // check cache
      PhysicalProduct pkg = null;

      if(useCache) {
          pkg = (PhysicalProduct) cache.get(id);
          if(pkg != null) {
              return pkg;
          }
      }

      pkg = new PhysicalProduct(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM physicalProduct WHERE productGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setConceptualProductGuid(rs.getString(2));
            pkg.setSerial(rs.getString(3));
            pkg.setShelfLocation(rs.getString(4));
            pkg.setDatePurchased(rs.getString(5));
            pkg.setCost(Double.parseDouble(rs.getString(6)));
      } else {
          throw new DataException("bad PhysicalProduct read");
      }
      if(pkg.getConceptualProductGuid() != null && !pkg.getConceptualProductGuid().equals("")){
          ConceptualProduct c = ConceptualProductDAO.getInstance().read(pkg.getConceptualProductGuid(), conn);
          pkg.setConceptualProduct(c);
      }
      // do populate
     Product p = ProductDAO.getInstance().read(pkg.getId(), conn);
     pkg.populate(p);

      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache



    // return the object

      return pkg;
  }//read

      //////////////////////////////////////////////////////////////////////////////////////
  //
  //                    READ BY CODE
  //
  /////////////////////////////////////////////////////////////////////////////////////


    public List<PhysicalProduct> readByCode(String code) throws DataException {
        Connection conn = cp.get();
        LinkedList<PhysicalProduct> products = new LinkedList();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid FROM physicalProduct WHERE serial LIKE ?");
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("productGuid");
                PhysicalProduct pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read cp read");
                }
                products.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read services by guid");
        } finally {
          cp.release(conn);
        }
        return products;
    }

  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(PhysicalProduct pkg) throws DataException {

      // get a jdbc connection
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
            throw new DataException("Problem saving PhysicalProduct", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(PhysicalProduct pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

        ProductDAO.getInstance().save(pkg, conn);

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
  private void update(PhysicalProduct pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE physicalProduct SET conceptualProductGuid=?, serial=?, shelfLocation=?, datePurchased=?, cost=? WHERE productGuid LIKE ?");
    
      pstmt.setString(1, pkg.getConceptualProductGuid());
      pstmt.setString(2, pkg.getSerial());
      pstmt.setString(3, pkg.getShelfLocation());
      pstmt.setString(4, pkg.getDatePurchased());
      pstmt.setDouble(5, pkg.getCost());
      pstmt.setString(6, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad PhysicalProduct update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(PhysicalProduct pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO physicalProduct VALUES (?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getConceptualProductGuid());
      pstmt.setString(3, pkg.getSerial());
      pstmt.setString(4, pkg.getShelfLocation());
      pstmt.setString(5, pkg.getDatePurchased());
      pstmt.setDouble(6, pkg.getCost());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad PhysicalProduct update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(PhysicalProduct pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<PhysicalProduct> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<PhysicalProduct> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid id FROM physicalProduct");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                PhysicalProduct pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read PhysicalProduct");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all PhysicalProduct");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }

 


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

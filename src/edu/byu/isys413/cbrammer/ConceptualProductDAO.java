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
public class ConceptualProductDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static ConceptualProductDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private ConceptualProductDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized ConceptualProductDAO getInstance() {
    if (instance == null) {
      instance = new ConceptualProductDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public ConceptualProduct create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      ConceptualProduct pkg = new ConceptualProduct(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public ConceptualProduct read(String id) throws DataException {
    // check cache
    ConceptualProduct pkg = (ConceptualProduct) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read ConceptualProduct", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized ConceptualProduct read(String id, Connection conn) throws Exception {
    // check cache
      ConceptualProduct pkg = (ConceptualProduct) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new ConceptualProduct(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM conceptualProduct WHERE productGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setName(rs.getString(2));
            pkg.setDescription(rs.getString(3));
            pkg.setManufacturer(rs.getString(4));
            pkg.setAverageCost(Double.parseDouble(rs.getString(5)));
            pkg.setUpc(rs.getString(6));
      } else {
          throw new DataException("bad conceptualProduct read");
      }

      Product p = ProductDAO.getInstance().read(pkg.getId(), conn);
      pkg.setPrice(p.getPrice());

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


    public List<ConceptualProduct> readByCode(String code) throws DataException {
        Connection conn = cp.get();
        LinkedList<ConceptualProduct> products = new LinkedList();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid FROM conceptualProduct WHERE upc LIKE ?");
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("productGuid");
                ConceptualProduct pkg;
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
  public void save(ConceptualProduct pkg) throws DataException {

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
            throw new DataException("Problem saving ConceptualProduct", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(ConceptualProduct pkg, Connection conn) throws Exception {
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
  private void update(ConceptualProduct pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE conceptualProduct SET name=?, description=?, manufacturer=?, averageCost=?, upc=? WHERE productGuid LIKE ?");
    
      pstmt.setString(1, pkg.getName());
      pstmt.setString(2, pkg.getDescription());
      pstmt.setString(3, pkg.getManufacturer());
      pstmt.setDouble(4, pkg.getAverageCost());
      pstmt.setString(5, pkg.getUpc());
      pstmt.setString(6, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ConceptualProduct update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(ConceptualProduct pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO conceptualProduct VALUES (?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getName());
      pstmt.setString(3, pkg.getDescription());
      pstmt.setString(4, pkg.getManufacturer());
      pstmt.setDouble(5, pkg.getAverageCost());
      pstmt.setString(6, pkg.getUpc());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ConceptualProduct update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(ConceptualProduct pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<ConceptualProduct> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<ConceptualProduct> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid id FROM conceptualProduct");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                ConceptualProduct pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read ConceptualProduct");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all ConceptualProduct");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

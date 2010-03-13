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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class ProductDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static ProductDAO instance = null;
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private ProductDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized ProductDAO getInstance() {
    if (instance == null) {
      instance = new ProductDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public Product create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Product pkg = new Product(id);
      pkg.setObjectAlreadyInDB(false);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Product read(String id) throws DataException {
    Product pkg;
    
    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Product", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Product read(String id, Connection conn) throws Exception {
    
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM product WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      Product pkg = new Product(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setPrice(Double.parseDouble(rs.getString(2)));
      } else {
          throw new DataException("bad Product read");
      }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);

    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Product pkg) throws DataException {

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
            throw new DataException("Problem saving Product", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(Product pkg, Connection conn) throws Exception {
   
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
  private void update(Product pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE product SET price=? WHERE guid LIKE ?");
    
      pstmt.setDouble(1, pkg.getPrice());
      pstmt.setString(2, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Product update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(Product pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO product VALUES (?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setDouble(2, pkg.getPrice());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Product update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Product pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Product> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<Product> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid id FROM product");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                Product pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read product");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all product");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

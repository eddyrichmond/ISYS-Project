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
public class EmployeeDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static EmployeeDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private EmployeeDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized EmployeeDAO getInstance() {
    if (instance == null) {
      instance = new EmployeeDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Employee create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Employee pkg = new Employee(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create

  public Employee readUserByUsername(String username) throws DataException {
      Employee pkg;
      // get a jdbc connection
      Connection conn = cp.get();
      try {
          // call the other read method
          pkg = readUserByUsername(username, conn);
      } catch (Exception ex) {
          throw new DataException("can't read employee", ex);
      } finally {
          cp.release(conn);
      }
      return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Employee readUserByUsername(String username, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("SELECT guid FROM employee WHERE username = ?");
      pstmt.setString(1,username );
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
          return EmployeeDAO.getInstance().read(rs.getString("guid"));
      } else {
          return null;
      }
  }//read


  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Employee read(String id) throws DataException {
    // check cache
    Employee pkg = (Employee) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read employee", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Employee read(String id, Connection conn) throws Exception {
    // check cache
      Employee pkg = (Employee) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new Employee(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM employee WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setStoreGuid(rs.getString(2));
            pkg.setUsername(rs.getString(3));
            pkg.setFirstName(rs.getString(4));
            pkg.setLastName(rs.getString(5));
            pkg.setHireDate(rs.getString(6));
            pkg.setPhone(rs.getString(7));
            pkg.setSalary(Double.parseDouble(rs.getString(8)));
            pkg.setCommissionRate(Double.parseDouble(rs.getString(9)));
      } else {
          throw new DataException("bad employee read");
      }
      if(pkg.getStoreGuid() != null && !pkg.getStoreGuid().equals("")){
          Store s = StoreDAO.getInstance().read(pkg.getStoreGuid(), conn);
          pkg.setStore(s);
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
  public void save(Employee pkg) throws DataException {

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
            throw new DataException("Problem saving Employee", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Employee pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);
     
//        if(pkg.getStore() != null)
//            StoreDAO.getInstance().save(pkg.getStore(), conn);

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
  private void update(Employee pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE employee SET storeGuid=?, username=?, firstName=?, lastName=?, hireDate=?, phone=?, salary=?, commissionRate=? WHERE guid LIKE ?");
    
      pstmt.setString(1, pkg.getStoreGuid());
      pstmt.setString(2, pkg.getUsername());
      pstmt.setString(3, pkg.getFirstName());
      pstmt.setString(4, pkg.getLastName());
      pstmt.setString(5, pkg.getHireDate());
      pstmt.setString(6, pkg.getPhone());
      pstmt.setString(7, pkg.getSalary().toString());
      pstmt.setString(8, pkg.getCommissionRate().toString());
      pstmt.setString(9, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad employee update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Employee pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getStoreGuid());
      pstmt.setString(3, pkg.getUsername());
      pstmt.setString(4, pkg.getFirstName());
      pstmt.setString(5, pkg.getLastName());
      pstmt.setString(6, pkg.getHireDate());
      pstmt.setString(7, pkg.getPhone());
      pstmt.setDouble(8, pkg.getSalary());
      pstmt.setDouble(9, pkg.getCommissionRate());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad employee update");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Employee pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Employee> getAll() throws DataException {


      Connection conn = cp.get();
      LinkedList<Employee> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid FROM employee");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("guid");
                Employee pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read emp");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all emp");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

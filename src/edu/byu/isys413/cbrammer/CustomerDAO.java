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
public class CustomerDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static CustomerDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private CustomerDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized CustomerDAO getInstance() {
    if (instance == null) {
      instance = new CustomerDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Customer create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Customer pkg = new Customer(id);
      pkg.setFirstName("New");
      pkg.setLastName("Customer");
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Customer read(String id) throws DataException {
    // check cache
    Customer pkg = (Customer) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read customer", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Customer read(String id, Connection conn) throws Exception {
    // check cache
      Customer pkg = (Customer) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM customer WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new Customer(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setFirstName(rs.getString("firstname"));
            pkg.setLastName(rs.getString("lastname"));
            pkg.setPhone(rs.getString("phone"));
            pkg.setEmail(rs.getString("email"));
            pkg.setAddress(rs.getString("address"));
            pkg.setMembershipGuid(rs.getString("membershipGuid"));

      } else {
          throw new DataException("bad customer read");
      }

      if(pkg.getMembershipGuid() != null && !pkg.getMembershipGuid().equals("")) {
          pkg.setMembership(MembershipDAO.getInstance().read(pkg.getMembershipGuid(), conn));
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
  public void save(Customer pkg) throws DataException {

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
            throw new DataException("Problem saving Customer", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Customer pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);
     


        Boolean dirtyFlag = pkg.isDirty();
        Boolean inDB = pkg.isObjectAlreadyInDB();

        if(pkg.getMembership() != null)
            MembershipDAO.getInstance().save(pkg.getMembership(), conn);

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
  private void update(Customer pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE customer SET firstName=?, lastName=?, phone=?, email=?, address=?, membershipGuid=? WHERE guid LIKE ?");
    
      pstmt.setString(1, pkg.getFirstName());
      pstmt.setString(2, pkg.getLastName());
      pstmt.setString(3, pkg.getPhone());
      pstmt.setString(4, pkg.getEmail());
      pstmt.setString(5, pkg.getAddress());
      pstmt.setString(6, pkg.getMembershipGuid());
      pstmt.setString(7, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad customer update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Customer pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO customer VALUES (?,?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
       pstmt.setString(2, pkg.getFirstName());
      pstmt.setString(3, pkg.getLastName());
      pstmt.setString(4, pkg.getPhone());
      pstmt.setString(5, pkg.getEmail());
      pstmt.setString(6, pkg.getAddress());
      pstmt.setString(7, pkg.getMembershipGuid());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad customer insert");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Customer pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Customer> getAll() throws DataException {
    // get a jdbc connection

      Connection conn = cp.get();
      LinkedList<Customer> customers = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid FROM customer");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("guid");
                Customer c;
                try {
                    c = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read cust");
                }
                customers.add(c);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all customers");
        } finally {
          cp.release(conn);
        }
        return customers;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

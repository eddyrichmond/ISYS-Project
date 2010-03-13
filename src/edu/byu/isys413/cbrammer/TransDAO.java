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
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class TransDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static TransDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private TransDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized TransDAO getInstance() {
    if (instance == null) {
      instance = new TransDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Trans create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Trans pkg = new Trans(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Trans read(String id) throws DataException {
    // check cache
    Trans pkg = (Trans) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Trans", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Trans read(String id, Connection conn) throws Exception {
    // check cache
      Trans pkg = (Trans) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new Trans(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM trans WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setEmpGuid(rs.getString(2));
            pkg.setStoreGuid(rs.getString(3));
            pkg.setCustomerGuid(rs.getString(4));
            pkg.setPaymentGuid(rs.getString(5));
            pkg.setJournalGuid(rs.getString(6));
            String myDate = rs.getString(7);
            Timestamp stamp = Timestamp.valueOf(myDate);

            pkg.setDate(myDate);

      } else {
          throw new DataException("bad Store read");
      }
      // EMPLOYEE
      if(pkg.getEmpGuid() != null && !pkg.getEmpGuid().equals(""))
        pkg.setEmployee(EmployeeDAO.getInstance().read(pkg.getEmpGuid(), conn));

      // STORE
      if(pkg.getStoreGuid() != null && !pkg.getStoreGuid().equals(""))
        pkg.setStore(StoreDAO.getInstance().read(pkg.getStoreGuid(), conn));

      // CUSTOMER
      if(pkg.getCustomerGuid() != null && !pkg.getCustomerGuid().equals(""))
        pkg.setCustomer(CustomerDAO.getInstance().read(pkg.getCustomerGuid(), conn));

      // PAYMENT
      if(pkg.getPaymentGuid() != null && !pkg.getPaymentGuid().equals(""))
        pkg.setPayment(PaymentDAO.getInstance().read(pkg.getPaymentGuid(), conn));

      // JOURNAL
      if(pkg.getJournalGuid() != null && !pkg.getJournalGuid().equals(""))
        pkg.setJournal(JournalEntryDAO.getInstance().read(pkg.getJournalGuid(), conn));


      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache

      

    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Trans pkg) throws DataException {

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
            throw new DataException("Problem saving Trans", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Trans pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);
        for(TransLine transLine:pkg.getTransList()) {
            TransLineDAO.getInstance().save(transLine, conn);
        }
        if(pkg.getEmployee() != null)
            EmployeeDAO.getInstance().save(pkg.getEmployee(), conn);
        if(pkg.getStore() != null)
            StoreDAO.getInstance().save(pkg.getStore(), conn);
        if(pkg.getCustomer() != null)
            CustomerDAO.getInstance().save(pkg.getCustomer(), conn);
        if(pkg.getPayment() != null)
            PaymentDAO.getInstance().save(pkg.getPayment(), conn);
        if(pkg.getJournal() != null)
            JournalEntryDAO.getInstance().save(pkg.getJournal(), conn);
        


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
  private void update(Trans pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE trans SET empGuid=?, storeGuid=?, customerGuid=?, paymentGuid=?, journalGuid=?, date=? WHERE guid LIKE ?");


      pstmt.setString(1, pkg.getEmpGuid());
      pstmt.setString(2, pkg.getStoreGuid());
      pstmt.setString(3, pkg.getCustomerGuid());
      pstmt.setString(4, pkg.getPaymentGuid());
      pstmt.setString(5, pkg.getJournalGuid());
      pstmt.setString(6, pkg.getDate());
      pstmt.setString(7, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Trans update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Trans pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO trans VALUES (?,?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getEmpGuid());
      pstmt.setString(3, pkg.getStoreGuid());
      pstmt.setString(4, pkg.getCustomerGuid());
      pstmt.setString(5, pkg.getPaymentGuid());
      pstmt.setString(6, pkg.getJournalGuid());
      pstmt.setString(7, pkg.getDate());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Trans insert");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Trans pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Trans> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<Trans> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid id FROM trans");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                Trans pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read Trans");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all Trans");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }

  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

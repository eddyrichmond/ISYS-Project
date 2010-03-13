/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import edu.byu.isys413.cbrammer.BusinessObject;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.byu.isys413.cbrammer.JournalEntryDAO;

/**
 *
 * @author cbrammer
 */
public class AccountingDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static AccountingDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private AccountingDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized AccountingDAO getInstance() {
    if (instance == null) {
      instance = new AccountingDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public Accounting create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Accounting pkg = new Accounting(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Accounting read(String id) throws DataException {
    // check cache
    Accounting pkg = (Accounting) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read accouting", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Accounting read(String id, Connection conn) throws Exception {
    // check cache
      Accounting pkg = (Accounting) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      
      // put in the cache
      pkg = new Accounting(id);
      cache.put(pkg.getId(), pkg);

       // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM accounting WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setJournalGuid(rs.getString(2));
            pkg.setType(rs.getString(3));
            pkg.setDescription(rs.getString(4));
            pkg.setAmount(Double.parseDouble(rs.getString(5)));
      } else {
          throw new DataException("bad accounting read");
      }
      
      if(pkg.getJournalGuid() != null && !pkg.getJournalGuid().equals("")){
        JournalEntry j = JournalEntryDAO.getInstance().read(pkg.getJournalGuid(), conn);
        pkg.setJournalEntry(j);
      }
      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
      
    // return the object

      return pkg;
  }//read


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Accounting pkg) throws DataException {

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
            throw new DataException("Problem saving Accounting", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(Accounting pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

//        if(pkg.getJournalEntry() != null)
//            JournalEntryDAO.getInstance().save(pkg.getJournalEntry(), conn);

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
  private void update(Accounting pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE accounting SET journalGuid=?, type=?, description=?, amount=? WHERE guid LIKE ?");
    
      pstmt.setString(1, pkg.getJournalGuid());
      pstmt.setString(2, pkg.getType());
      pstmt.setString(3, pkg.getDescription());
      pstmt.setDouble(4, pkg.getAmount());
      pstmt.setString(5, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Accounting update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(Accounting pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO accounting VALUES (?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getJournalGuid());
      pstmt.setString(3, pkg.getType());
      pstmt.setString(4, pkg.getDescription());
      pstmt.setDouble(5, pkg.getAmount());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad accounting update");
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

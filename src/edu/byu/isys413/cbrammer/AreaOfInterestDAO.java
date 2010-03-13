/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author Garrett
 */
public class AreaOfInterestDAO {

      /////////////////////////////////////////////
  ///   Singleton code

  private static AreaOfInterestDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private AreaOfInterestDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized AreaOfInterestDAO getInstance() {
    if (instance == null) {
      instance = new AreaOfInterestDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public AreaOfInterest create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      AreaOfInterest pkg = new AreaOfInterest(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;

  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public AreaOfInterest read(String id) throws DataException {
    // check cache
    AreaOfInterest pkg = (AreaOfInterest) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read AreaOfInterest", ex);
    } finally {
        cp.release(conn);
    }

    // use pkg finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized AreaOfInterest read(String id, Connection conn) throws Exception {
    // check cache
      AreaOfInterest pkg = (AreaOfInterest) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM AreaOfInterest WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new AreaOfInterest(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setDescription(rs.getString(2));
            
      } else {
          throw new DataException("bad AreaOfInterest read");
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
  public void save(AreaOfInterest pkg) throws DataException {

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
            throw new DataException("Problem saving AreaOfInterest", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(AreaOfInterest pkg, Connection conn) throws Exception {
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
  private void update(AreaOfInterest pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE AreaOfInterest SET description=? WHERE guid LIKE ?");

      pstmt.setString(1, pkg.getDescription());
      pstmt.setString(2, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad AreaOfInterest update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(AreaOfInterest pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO AreaOfInterest VALUES (?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getDescription());
      

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad AreaOfInterest update");
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

 


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


  public java.util.List<AreaOfInterest> getAll() throws DataException {
    // get a jdbc connection

      Connection conn = cp.get();
      LinkedList<AreaOfInterest> areas = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid FROM areaOfInterest");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("guid");
                AreaOfInterest a;
                try {
                    a = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read areas");
                }
                areas.add(a);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all areas");
        } finally {
          cp.release(conn);
        }
        return areas;
  }

    
    
}

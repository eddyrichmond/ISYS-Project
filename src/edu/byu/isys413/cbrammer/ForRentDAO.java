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
import java.util.List;

/**
 *
 * @author Garrett
 */
public class ForRentDAO {

      /////////////////////////////////////////////
  ///   Singleton code

  private static ForRentDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private ForRentDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized ForRentDAO getInstance() {
    if (instance == null) {
      instance = new ForRentDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public ForRent create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      ForRent pkg = new ForRent(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;

  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public ForRent read(String id) throws DataException {
    // check cache
    ForRent pkg = (ForRent) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read ForRent", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized ForRent read(String id, Connection conn) throws Exception {
    // check cache
      ForRent pkg = (ForRent) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new ForRent(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ForRent WHERE productGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setTimesRented(Integer.parseInt(rs.getString(2)));
            pkg.setIsRented(Boolean.parseBoolean(rs.getString(3)));

      } else {
          throw new DataException("bad ForRent read");
      }


      // do populate

      PhysicalProduct pp = PhysicalProductDAO.getInstance().read(pkg.getId(), conn, Boolean.FALSE);
      pkg.populate(pp);

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


    public List<ForRent> readByCode(String code) throws DataException {
        Connection conn = cp.get();
        LinkedList<ForRent> rentals = new LinkedList();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid FROM PhysicalProduct WHERE serial LIKE ?");
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("productGuid");
                ForRent pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read forRent ");
                }
                rentals.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read services by guid");
        } finally {
          cp.release(conn);
        }
        return rentals;
    }

  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(ForRent pkg) throws DataException {

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
            throw new DataException("Problem saving ForRent", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(ForRent pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);


        Boolean dirtyFlag = pkg.isDirty();
        Boolean inDB = pkg.isObjectAlreadyInDB();

        PhysicalProductDAO.getInstance().save(pkg, conn);

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
  private void update(ForRent pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE ForRent SET timesRented=?, isRented=? WHERE productGuid LIKE ?");

      pstmt.setInt(1, pkg.getTimesRented());
      pstmt.setBoolean(2, pkg.isIsRented());
      pstmt.setString(3, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ForRent update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(ForRent pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ForRent VALUES (?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setInt(2, pkg.getTimesRented());
      pstmt.setBoolean(3, pkg.isIsRented());


      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ForRent update");
      }
      pstmt.close();

  }





  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(ForRent pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<ForRent> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<ForRent> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid id FROM ForRent");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                ForRent pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read ForRent");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all ForRent");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }




  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.





}

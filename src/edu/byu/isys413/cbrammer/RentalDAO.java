/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class RentalDAO {

    /////////////////////////////////////////////
  ///   Singleton code

  private static RentalDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates pkg new instance of SkeletonDAO */
  private RentalDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized RentalDAO getInstance() {
    if (instance == null) {
      instance = new RentalDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates pkg new pkg in the database */
  public Rental create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Rental pkg = new Rental(id);
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
  public Rental read(String id) throws DataException {
    // check cache
    Rental pkg = (Rental) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get pkg jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Rental", ex);
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
  synchronized Rental read(String id, Connection conn) throws Exception {
    // check cache
      Rental pkg = (Rental) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Rental WHERE revenueSourceGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new Rental(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            if(rs.getString(2) != null)
                pkg.setDateOut(Timestamp.valueOf(rs.getString(2)));
            if(rs.getString(3) != null)
                pkg.setDateIn(Timestamp.valueOf(rs.getString(3)));
            if(rs.getString(4) != null)
                pkg.setDateDue(Timestamp.valueOf(rs.getString(4)));
            pkg.setAmount(Double.parseDouble(rs.getString(5)));
            pkg.setForRentGuid(rs.getString(6));

      } else {
          throw new DataException("bad Rental read");
      }

      if(pkg.getForRentGuid() != null && !pkg.getForRentGuid().equals("")) {
          pkg.setForRent(ForRentDAO.getInstance().read(pkg.getForRentGuid(), conn));
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
  public void save(Rental pkg) throws DataException {

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
            throw new DataException("Problem saving Rental", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update pkg pkg in the database */
  void save(Rental pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);

        Boolean dirtyFlag = pkg.isDirty();
        Boolean inDB = pkg.isObjectAlreadyInDB();

        RevenueSourceDAO.getInstance().save(pkg, conn);
        
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
  private void update(Rental pkg, Connection conn) throws Exception {
      //PreparedStatement pstmt = conn.prepareStatement("UPDATE Rental SET dateOut=?, dateIn=?, dateDue=?, amount=? WHERE revenueSourceGuid LIKE ?");
      PreparedStatement pstmt = conn.prepareStatement("UPDATE rental SET dateOut=?, dateIn=?, dueDate=?, amount=?, forRentGuid=? WHERE revenueSourceGuid LIKE ?");

      pstmt.setTimestamp(1, pkg.getDateOut());
      pstmt.setTimestamp(2, pkg.getDateIn());
      pstmt.setTimestamp(3, pkg.getDateDue());
      pstmt.setDouble(4, pkg.getAmount());
      pstmt.setString(5, pkg.getForRentGuid());
      pstmt.setString(6, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Rental update");
      }

      pstmt.close();
  }

  /** Inserts pkg new pkg into the database */
  private void insert(Rental pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO rental VALUES (?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setTimestamp(2, pkg.getDateOut());
      pstmt.setTimestamp(3, pkg.getDateIn());
      pstmt.setTimestamp(4, pkg.getDateDue());
      pstmt.setDouble(5, pkg.getAmount());
      pstmt.setString(6, pkg.getForRentGuid());
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Rental update");
      }
      pstmt.close();

  }





  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Rental pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Rental> getAll() throws DataException {
    // get a jdbc connection

      Connection conn = cp.get();
      LinkedList<Rental> rentals = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid FROM rental");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("guid");
                Rental r;
                try {
                    r = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read rental");
                }
                rentals.add(r);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all rentals");
        } finally {
          cp.release(conn);
        }
        return rentals;
  }
  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.

//  public Rental readByCode(String code) throws DataException {
//        Connection conn = cp.get();
//        LinkedList<Rental> services = new LinkedList();
//        PreparedStatement pstmt;
//        try {
//            pstmt = conn.prepareStatement("SELECT forRentGuid FROM rental WHERE barcode LIKE ?");
//            pstmt.setString(1, code);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                String forRentGuid = rs.getString("forRentGuid");
//                Service s;
//
//            pstmt = conn.prepareStatement("SELECT forRentGuid FROM rental WHERE barcode LIKE ?");
//            pstmt.setString(1, code);
//            ResultSet rs = pstmt.executeQuery();
//            while (rs.next()) {
//                String forRentGuid = rs.getString("forRentGuid");
//                Service s;
//                try {
//                    s = read(myGuid, conn);
//                } catch (Exception ex) {
//                   throw new DataException("bad read service");
//                }
//                services.add(s);
//            }
//        } catch (SQLException ex) {
//            throw new DataException("bad read services by guid");
//        } finally {
//          cp.release(conn);
//        }
//        return services;
//    }

    public java.util.List<Rental> loadLateRentals(Timestamp stamp) throws DataException {
    // get a jdbc connection

      Connection conn = cp.get();
      LinkedList<Rental> rentals = new LinkedList();
      String s = stamp.toString();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("select revenueSourceGuid from RENTAL r where r.DATEIN IS NULL AND r.DUEDATE < ?");
            pstmt.setTimestamp(1, stamp);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("revenueSourceGuid");
                Rental r;
                try {
                    r = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read rental");
                }
                rentals.add(r);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all rentals");
        } finally {
          cp.release(conn);
        }
        return rentals;
  }




}

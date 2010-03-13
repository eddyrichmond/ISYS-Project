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
public class ConceptualRentalDAO {

      /////////////////////////////////////////////
  ///   Singleton code

  private static ConceptualRentalDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private ConceptualRentalDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized ConceptualRentalDAO getInstance() {
    if (instance == null) {
      instance = new ConceptualRentalDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public ConceptualRental create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      ConceptualRental pkg = new ConceptualRental(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;

  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public ConceptualRental read(String id) throws DataException {
    // check cache
    ConceptualRental pkg = (ConceptualRental) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read ConceptualRental", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized ConceptualRental read(String id, Connection conn) throws Exception {
    // check cache
      ConceptualRental pkg = (ConceptualRental) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new ConceptualRental(id);
      cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ConceptualRental WHERE productGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setReplacementPrice(Double.parseDouble(rs.getString(2)));
            pkg.setPricePerDay(Double.parseDouble(rs.getString(3)));

      } else {
          throw new DataException("bad ConceptualRental read");
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

 public List<ConceptualRental> readByCode(String code) throws DataException {
        Connection conn = cp.get();
        LinkedList<ConceptualRental> rentals = new LinkedList();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid FROM conceptualProduct WHERE upc LIKE ?");
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("productGuid");
                ConceptualRental pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read cr read");
                }
                rentals.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read first try/catch in ready by code for conceptual rental");
        } finally {
          cp.release(conn);
        }
        return rentals;
    }


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(ConceptualRental pkg) throws DataException {

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
            throw new DataException("Problem saving ConceptualRental", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(ConceptualRental pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);


        Boolean dirtyFlag = pkg.isDirty();
        Boolean inDB = pkg.isObjectAlreadyInDB();

        ConceptualProductDAO.getInstance().save(pkg, conn);
        
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
  private void update(ConceptualRental pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE ConceptualRental SET replacementPrice=?, pricePerDay=? WHERE productGuid LIKE ?");

      pstmt.setDouble(1, pkg.getReplacementPrice());
      pstmt.setDouble(2, pkg.getPricePerDay());
      pstmt.setString(3, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ConceptualRental update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(ConceptualRental pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ConceptualRental VALUES (?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setDouble(2, pkg.getReplacementPrice());
      pstmt.setDouble(3, pkg.getPricePerDay());


      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad ConceptualRental update");
      }
      pstmt.close();

  }





  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(ConceptualRental pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<ConceptualRental> getAll() throws DataException {
      Connection conn = cp.get();
      LinkedList<ConceptualRental> listAll = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT productGuid id FROM ConceptualRental");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString(1);
                ConceptualRental pkg;
                try {
                    pkg = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read ConceptualRental");
                }
                listAll.add(pkg);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all ConceptualRental");
        } finally {
          cp.release(conn);
        }
        return listAll;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.




}

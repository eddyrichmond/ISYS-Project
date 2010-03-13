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
import java.util.LinkedList;

/**
 *
 * @author Garrett
 */
public class MembershipDAO {

 /////////////////////////////////////////////
  ///   Singleton code

  private static MembershipDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private MembershipDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized MembershipDAO getInstance() {
    if (instance == null) {
      instance = new MembershipDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Membership create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Membership pkg = new Membership(id);

      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;

  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Membership read(String id) throws DataException {
    // check cache
    Membership pkg = (Membership) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Membership", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Membership read(String id, Connection conn) throws Exception {
    // check cache
      Membership pkg = (Membership) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Membership WHERE guid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      pkg = new Membership(id);
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setCreditCardNumber(rs.getString("creditCardNumber"));
            pkg.setStartDate(Timestamp.valueOf(rs.getString("startDate")));
            pkg.setExpireDate(Timestamp.valueOf(rs.getString("expireDate")));

      } else {
          throw new DataException("bad Membership read");
      }

      pkg.setAreasOfInterestList(readMembershipAreasOfInterest(pkg.getId(), conn));


      pkg.setDirty(false);
      pkg.setObjectAlreadyInDB(true);
    // put in the cache

      cache.put(pkg.getId(), pkg);

    // return the object

      return pkg;
  }//read



  /** Internal method to read an existing pkg from the database */
  synchronized LinkedList<AreaOfInterest> readMembershipAreasOfInterest(String id, Connection conn) throws Exception {

    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM membership_areaOfInterest WHERE membershipGuid LIKE ?");
      pstmt.setString(1, id);
      
      LinkedList<AreaOfInterest> areasOfInterest = new LinkedList();

        try {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String areaGuid = rs.getString("areaOfInterestGuid");
                AreaOfInterest a;
                try {
                    a = AreaOfInterestDAO.getInstance().read(areaGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read area of interest");
                }
                areasOfInterest.add(a);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all areas");
        } finally {
          cp.release(conn);
        }
        return areasOfInterest;
  }//read


  void saveAllAreas(Membership pkg, Connection conn) throws Exception {
      for (AreaOfInterest a:pkg.getAreasOfInterestList()) {
          saveMemberAreaOfInterest(pkg.getId(), a, conn);
      }
  }
  
  /** Internal method to update a pkg in the database */
  void saveMemberAreaOfInterest(String id, AreaOfInterest pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("INSERT INTO membership_areaOfInterest VALUES (?,?)");
      pstmt.setString(1, id);
      pstmt.setString(2, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {

      } else {
          throw new DataException("bad Membership insert");
      }
      pstmt.close();
  }//save


  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Membership pkg) throws DataException {

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
            throw new DataException("Problem saving Membership", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Membership pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);


    // if not dirty, return
      if(!pkg.isDirty() && pkg.isObjectAlreadyInDB()) {
          return;
      }

      saveAllAreas(pkg, conn);

    // call either update() or insert()
        if(pkg.isObjectAlreadyInDB()) {
            update(pkg, conn);
        } else {
            insert(pkg, conn);
        }
  }//save

   /** Saves an existing pkg to the database */
  private void update(Membership pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE Membership SET creditCardNumber=?, startDate=?, expireDate=? WHERE guid LIKE ?");

      pstmt.setString(1, pkg.getCreditCardNumber());
      pstmt.setTimestamp(2, pkg.getStartDate());
      pstmt.setTimestamp(3, pkg.getExpireDate());
      pstmt.setString(4, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Membership update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Membership pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Membership VALUES (?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getCreditCardNumber());
      pstmt.setString(3, pkg.getStartDate().toString());
      pstmt.setString(4, pkg.getExpireDate().toString());

      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Membership insert");
      }
      pstmt.close();

  }





  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Membership pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Membership> getAll() throws DataException {
    // get a jdbc connection

      Connection conn = cp.get();
      LinkedList<Membership> Memberships = new LinkedList();
      PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT guid FROM Membership");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("guid");
                Membership c;
                try {
                    c = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read cust");
                }
                Memberships.add(c);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read all Memberships");
        } finally {
          cp.release(conn);
        }
        return Memberships;
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.




}

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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cbrammer
 */
public class ServiceDAO {

  /////////////////////////////////////////////
  ///   Singleton code

  private static ServiceDAO instance = null;
  private Cache cache = Cache.getInstance();
  private ConnectionPool cp = ConnectionPool.getInstance();

  /** Creates a new instance of SkeletonDAO */
  private ServiceDAO() {
  }//constructor

  /** Retrieves the single instance of this class */
  public static synchronized ServiceDAO getInstance() {
    if (instance == null) {
      instance = new ServiceDAO();
    }
    return instance;
  }//getInstance


  ////////////////////////////////////////////
  ///   CREATE methods

  /** Creates a new pkg in the database */
  public Service create(String id) throws DataException {
    // create new BO, set whether in the DB or not
      Service pkg = new Service(id);
      pkg.setObjectAlreadyInDB(false);

    // put into the cache
      cache.put(pkg.id, pkg);

    // return the new object
      return pkg;
      
  }//create



  ////////////////////////////////////////////
  ///   READ methods

  /** Reads an existing pkg from the database */
  public Service read(String id) throws DataException {
    // check cache
    Service pkg = (Service) cache.get(id);
    if(pkg != null) {
        return pkg;
    }

    // get a jdbc connection
    Connection conn = cp.get();
    try {
        // call the other read method
        pkg = read(id, conn);
    } catch (Exception ex) {
        throw new DataException("can't read Service", ex);
    } finally {
        cp.release(conn);
    }

    // use a finally clause to release the connection
    // return the object
    return pkg;
  }

  /** Internal method to read an existing pkg from the database */
  synchronized Service read(String id, Connection conn) throws Exception {
    // check cache
      Service pkg = (Service) cache.get(id);
      if(pkg != null) {
          return pkg;
      }
      pkg = new Service(id);
       cache.put(pkg.getId(), pkg);
    // pull from database and populate the object
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM service WHERE revenuesourceGuid LIKE ?");
      pstmt.setString(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
            pkg.setEmployeeGuid(rs.getString("employeeguid"));
            pkg.setAmount(Double.parseDouble(rs.getString("amount")));
            pkg.setLaborHours(Double.parseDouble(rs.getString("laborhours")));
            pkg.setDescription(rs.getString("description"));
            pkg.setDateStarted(rs.getString("datestarted"));
            pkg.setDateCompleted(rs.getString("datecompleted"));
            pkg.setDatePickedUp(rs.getString("datepickedup"));
            pkg.setBarcode(rs.getString("barcode"));


      } else {
          throw new DataException("bad Service read");
      }

      if(pkg.getEmployeeGuid() != null && !pkg.getEmployeeGuid().equals("")){
          Employee e = EmployeeDAO.getInstance().read(pkg.getEmployeeGuid(), conn);
          pkg.setEmployee(e);
      }

      RevenueSource rev = RevenueSourceDAO.getInstance().read(id, conn);
      pkg.setTransLineGuid(rev.getTransLineGuid());
      pkg.setTransLine(rev.getTransLine());
      pkg.setType(rev.getType());
      
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


    public List<Service> readByCode(String code) throws DataException {
        Connection conn = cp.get();
        LinkedList<Service> services = new LinkedList();
        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement("SELECT revenueSourceGuid FROM service WHERE barcode LIKE ?");
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String myGuid = rs.getString("revenueSourceGuid");
                Service s;
                try {
                    s = read(myGuid, conn);
                } catch (Exception ex) {
                   throw new DataException("bad read service");
                }
                services.add(s);
            }
        } catch (SQLException ex) {
            throw new DataException("bad read services by guid");
        } finally {
          cp.release(conn);
        }
        return services;
    }

  /////////////////////////////////////////////
  ///   UPDATE methods

  /** Saves an existing pkg in the database */
  public void save(Service pkg) throws DataException {

      // get a jdbc connection
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
            throw new DataException("Problem saving Service", e);
      } finally {
          ConnectionPool.getInstance().release(conn);
      }
  }//update

  /** Internal method to update a pkg in the database */
  void save(Service pkg, Connection conn) throws Exception {
    // update the cache
        cache.put(pkg.getId(), pkg);
//        
//        if(pkg.getEmployee() != null)
//            EmployeeDAO.getInstance().save(pkg.getEmployee(), conn);

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
  private void update(Service pkg, Connection conn) throws Exception {
      PreparedStatement pstmt = conn.prepareStatement("UPDATE service SET employeeGuid=?, amount=?, laborHours=?, barcode=?, description=?, dateStarted=?, dateCompleted=?, datePickedUp=? WHERE revenuesourceGuid LIKE ?");
    
      pstmt.setString(1, pkg.getEmployeeGuid());
      pstmt.setDouble(2, pkg.getAmount());
      pstmt.setDouble(3, pkg.getLaborHours());
      pstmt.setString(4, pkg.getBarcode());
      pstmt.setString(5, pkg.getDescription());
      pstmt.setString(6, pkg.getDateStarted());
      pstmt.setString(7, pkg.getDateCompleted());
      pstmt.setString(8, pkg.getDatePickedUp());
      pstmt.setString(9, pkg.getId());

      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Service update");
      }

      pstmt.close();
  }

  /** Inserts a new pkg into the database */
  private void insert(Service pkg, Connection conn) throws Exception {
    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO service VALUES (?,?,?,?,?,?,?,?,?)");
      pstmt.setString(1, pkg.getId());
      pstmt.setString(2, pkg.getEmployeeGuid());
      pstmt.setString(3, pkg.getBarcode());
      pstmt.setDouble(4, pkg.getAmount());
      pstmt.setDouble(5, pkg.getLaborHours());
      pstmt.setString(6, pkg.getDescription());
      pstmt.setString(7, pkg.getDateStarted());
      pstmt.setString(8, pkg.getDateCompleted());
      pstmt.setString(9, pkg.getDatePickedUp());
      
      int numUpd = pstmt.executeUpdate();

      if(numUpd == 1) {
            pkg.setObjectAlreadyInDB(true);
            pkg.setDirty(false);
      } else {
          throw new DataException("bad Service insert");
      }
      pstmt.close();
       
  }

 



  /////////////////////////////////////////////////
  ///   DELETE methods

  /** We do not support deleting of business objects in this application */
  public void delete(Service pkg) throws DataException {
    throw new UnsupportedOperationException("Nice try. The delete function is not supported in this application.");
  }



  ////////////////////////////////////////////////
  ///   SEARCH methods

  /** Retrieves all prods from the database */
  public java.util.List<Service> getAll() throws DataException {
    // get a jdbc connection
    // get all BOs for this type and return them
    // be sure to use the read() method above so the cache is used automatically
    // use a finally clause to release the connection
    throw new UnsupportedOperationException();
  }


  // additional search methods go here.  examples are
  // getByName, getByProductCode, etc.


}

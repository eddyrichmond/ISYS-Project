package edu.byu.isys413.cbrammer;

import java.sql.*;

/**
 * Creates the database for the Manager's Miner assignment.
 *
 * Before running, you MUST create a database named "ManagersMiner"
 * with no username or password on it.  You also must add the
 * Java DB libraries to your project by right-clicking "Libraries" in your
 * project, selecting "Add Library" and selecting "Java DB Driver".
 *
 * @author Conan Albrecht <conan@warp.byu.edu>
 */
public class CreateDB {
    /** Database Name */
    public static final String DB_NAME = "sprint1";

    /**
     * Creates the database.  Please see the class-level
     * Javadoc above before running.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
      // first create the connection
      Class.forName("org.apache.derby.jdbc.ClientDriver");
      Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/" + DB_NAME);
      int errors = 0;
      for (String sql: SQL_COMMANDS) {
        try {
          if (sql.indexOf("CREATE TABLE") >= 0) {  // just a little status output
            System.out.println("New table: " + sql);
          }
          Statement stmt = conn.createStatement();
          stmt.executeUpdate(sql);
          stmt.close();
        }catch (SQLException e) {
          System.out.println("Error: " + e.getMessage() + " on >>>" + sql + "<<<");
          errors++;
          if (errors >= 10) {  // don't keep running if we're hitting errors every single time.
            System.out.println("Too many errors.  Exiting the program.");
            System.exit(1);
          }
        }//try
      }//for
      conn.commit();
      conn.close();
    }//main method


    /** An array containing all the SQL to run. Again, I don't like this
        way of doing things, but it keeps the class standalone without
        any external links and files, which is desirable in this case.
     */
    private static final String[] SQL_COMMANDS = {
      "DROP TABLE employee",
      "CREATE TABLE employee(guid VARCHAR(255), storeGuid VARCHAR(255), username VARCHAR(50), firstName VARCHAR(50), lastName VARCHAR(50), hireDate VARCHAR(255), phone VARCHAR(50), salary FLOAT, commissionRate FLOAT)",
      "INSERT INTO employee VALUES ('emp1','store1','cbrammer','Chase', 'Brammer', '2004-06-22 10:33:11.840', '(801) 369-1028', 100000, 10)",
      "INSERT INTO employee VALUES ('emp2','store2','gbarber','Garrett', 'Barber', '2004-06-22 10:33:11.840', '(850) 757-1571', 200000, 5)",
      "INSERT INTO employee VALUES ('emp3','store1','merrelld','Dan', 'Merrell', '2004-06-22 10:33:11.840', '(777) 369-1028', 150000, 10)",
      "INSERT INTO employee VALUES ('emp4','store2','erichmon','Eddy', 'Munster', '2004-06-22 10:33:11.840', '(999) 757-1571', 20000, 5)",

      "DROP TABLE store",
      "CREATE TABLE store(guid VARCHAR(255), location VARCHAR(255), managerGuid VARCHAR(255), address VARCHAR(255), phone VARCHAR(50))",
      "INSERT INTO store VALUES ('store1','Orem','emp1','123 State St.','801-225-4118')",
      "INSERT INTO store VALUES ('store2','Logan','emp2','789 Main St.','801-221-8243')",

      "DROP TABLE customer",
      "CREATE TABLE customer(guid VARCHAR(255), firstName VARCHAR(50), lastName VARCHAR(50), phone VARCHAR(50), email VARCHAR(50), address VARCHAR(255), membershipGuid VARCHAR(255))",
      "INSERT INTO customer VALUES ('cust1','Brady', 'Brammer', '801-111-1111', 'brady@brady.com', 'Somewhere in CA', 'membership1')",
      "INSERT INTO customer VALUES ('cust2','Chad', 'Brammer', '801-222-2222', 'chad@chad.com', 'Somewhere in UT', null)",

      "DROP TABLE trans",
      "CREATE TABLE trans(guid VARCHAR(255), empGuid VARCHAR(255), storeGuid VARCHAR(255), customerGuid VARCHAR(255), paymentGuid VARCHAR(255), journalGuid VARCHAR(255), date VARCHAR(255))",
      "INSERT INTO trans VALUES ('trans1', 'emp1', 'store1', 'cust1',  'pay1', 'j1', '2004-06-22 10:33:11.840')",
      "INSERT INTO trans VALUES ('trans2', 'emp2', 'store2', 'cust2',  'pay2', 'j2', '2004-06-22 10:33:11.840')",

      "DROP TABLE payment",
      "CREATE TABLE payment(guid VARCHAR(255), amount FLOAT, change FLOAT, type VARCHAR(255))",
      "INSERT INTO payment VALUES ('pay1', 101.00, 99.99, 'cash')",
      "INSERT INTO payment VALUES ('pay2', 1, 0, 'visa')",

      "DROP TABLE transLine",
      "CREATE TABLE transLine(guid VARCHAR(255), transGuid VARCHAR(255), revenueSourceGuid VARCHAR (255))",
      "INSERT INTO transLine VALUES ('line1', 'trans1','sp1')",
      "INSERT INTO transLine VALUES ('line2', 'trans2', 's1')",
      "INSERT INTO transLine VALUES ('line3', 'trans2', 'r1')",

      "DROP TABLE commission",
      "CREATE TABLE commission(guid VARCHAR(255), amount FLOAT, paid VARCHAR(255))",
      "INSERT INTO commission VALUES ('comm1', 20.00, '1')",
      "INSERT INTO commission VALUES ('comm2', 10.00, '1')",

      "DROP TABLE journalEntry",
      "CREATE TABLE journalEntry(guid VARCHAR(255))",
      "INSERT INTO journalEntry VALUES ('j1')",
      "INSERT INTO journalEntry VALUES ('j2')",

      "DROP TABLE accounting",
      "CREATE TABLE accounting(guid VARCHAR(255), journalGuid VARCHAR(255), type VARCHAR(50), description VARCHAR(25), amount FLOAT)",
      "INSERT INTO accounting VALUES ('acc1', 'j1', 'debit', 'description', 10)",
      "INSERT INTO accounting VALUES ('acc2', 'j2', 'credit', 'description', 20)",

      "DROP TABLE revenueSource",
      "CREATE TABLE revenueSource(guid VARCHAR(255), transLineGuid VARCHAR(255), type VARCHAR(50), commGuid VARCHAR(255))",
      "INSERT INTO revenueSource VALUES ('sp1', 'line1', 'service', 'comm1')",
      "INSERT INTO revenueSource VALUES ('sp2', 'line1', 'sale', 'comm1')",
      "INSERT INTO revenueSource VALUES ('s1', 'line2', 'service', 'comm1')",
      "INSERT INTO revenueSource VALUES ('s2', 'line2', 'service', 'comm2')",
      "INSERT INTO revenueSource VALUES ('r1', 'line1', 'rental', 'comm2')",
      "INSERT INTO revenueSource VALUES ('r2', 'line1', 'rental', 'comm2')",
      "INSERT INTO revenueSource VALUES ('r3', 'line1', 'rental', 'comm2')",
      "INSERT INTO revenueSource VALUES ('r4', 'line1', 'rental', 'comm2')",
      "INSERT INTO revenueSource VALUES ('r5', 'line1', 'rental', 'comm2')",

      "DROP TABLE saleProduct",
      "CREATE TABLE saleProduct(revenueSourceGuid VARCHAR(255), quantity INTEGER, productGuid VARCHAR(255))",
      "INSERT INTO saleProduct VALUES ('sp1', 5, 'phys1')",
      "INSERT INTO saleProduct VALUES ('sp2', 4, 'conc1')",

      "DROP TABLE service",
      "CREATE TABLE service(revenueSourceGuid VARCHAR(255), employeeGuid VARCHAR(255), barcode VARCHAR(255), amount FLOAT, laborHours FLOAT, description VARCHAR(255), dateStarted VARCHAR(255), dateCompleted VARCHAR(255), datePickedUp VARCHAR(255))",
      "INSERT INTO service VALUES ('s1', 'emp1','barcode1', 100.00, 2, 'this is a desc', '2004-06-22 10:33:11.840', '2004-06-22 10:33:11.840', '2004-06-22 10:33:11.840')",
      "INSERT INTO service VALUES ('s2', 'emp2','barcode2', 100.00, 2, 'this is a desc', '2004-06-22 10:33:11.840', '2004-06-22 10:33:11.840', '2004-06-22 10:33:11.840')",

      "DROP TABLE product",
      "CREATE TABLE product(guid VARCHAR(255), price FLOAT)",
      "INSERT INTO product VALUES ('phys1', 200)",
      "INSERT INTO product VALUES ('conc1', 999)",

      "DROP TABLE conceptualProduct",
      "CREATE TABLE conceptualProduct(productGuid VARCHAR(255), name VARCHAR(255), description VARCHAR(255), manufacturer VARCHAR(255), averageCost FLOAT, upc VARCHAR(255))",
      "INSERT INTO conceptualProduct VALUES ('conc1', 'my product', 'this is my desc', 'ABC Manf.', 200.05, 'upc')",
      "INSERT INTO conceptualProduct VALUES ('conc2', 'my product2', 'this is my desc2', 'ABC Manf.2', 202.05, 'upc2')",

      "DROP TABLE physicalProduct",
      "CREATE TABLE physicalProduct(productGuid VARCHAR(255), conceptualProductGuid VARCHAR(255), serial VARCHAR(255), shelfLocation VARCHAR(255), datePurchased VARCHAR(255), cost FLOAT)",
      "INSERT INTO physicalProduct VALUES ('phys1', 'conc1', '123', '2b', '2004-06-22 10:33:11.840', 250.00)",

      "DROP TABLE conceptualRental",
      "CREATE TABLE conceptualRental(productGuid VARCHAR(255), replacementPrice FLOAT, pricePerDay FLOAT)",
      "INSERT INTO conceptualRental VALUES ('conc1', 200, 20)",

      "DROP TABLE rental",
      "CREATE TABLE rental(revenueSourceGuid VARCHAR(255), dateOut TIMESTAMP, dateIn TIMESTAMP, dueDate TIMESTAMP, amount FLOAT, forRentGuid VARCHAR(255))",
      "INSERT INTO rental VALUES ('r1', '2005-06-22 10:33:11.840', null, '2005-06-28 10:33:11.840', 20, 'phys1')",
      "INSERT INTO rental VALUES ('r2', '2004-06-22 10:33:11.840', '2004-07-22 10:33:11.840', '2004-06-27 10:33:11.840', 20, 'phys1')",
      "INSERT INTO rental VALUES ('r3', '2009-06-22 10:33:11.840', '2009-06-28 10:33:11.840', '2009-06-28 10:33:11.840', 20, 'phys1')",
      "INSERT INTO rental VALUES ('r4', '2009-06-22 10:33:11.840', null, '2009-06-28 10:33:11.840', 20, 'phys1')",
      "INSERT INTO rental VALUES ('r5', '2009-06-22 10:33:11.840', null, '2009-06-28 10:33:11.840', 20, 'phys1')",

      "DROP TABLE forRent",
      "CREATE TABLE forRent(productGuid VARCHAR(255), timesRented INTEGER, isRented VARCHAR(1))",
      "INSERT INTO forRent VALUES ('phys1', 13, 'n')",

      "DROP TABLE membership",
      "CREATE TABLE membership(guid VARCHAR(255), creditCardNumber VARCHAR(255), startDate TIMESTAMP, expireDate TIMESTAMP)",
      "INSERT INTO membership VALUES ('membership1', '123456789', '2004-06-22 10:33:11.840', '2004-06-22 10:33:11.840')",

      "DROP TABLE areaOfInterest",
      "CREATE TABLE areaOfInterest( guid VARCHAR(255), description VARCHAR(255))",
      "INSERT INTO areaOfInterest VALUES ('areaOfInterest1', 'Bobsledding naked in Africa')",

      "DROP TABLE membership_areaOfInterest",
      "CREATE TABLE membership_areaOfInterest( membershipGuid VARCHAR(255), areaOfInterestGuid VARCHAR(255))",
      "INSERT INTO membership_areaOfInterest VALUES ('membership1', 'areaOfInterest1')",

      "DROP TABLE commissionFactor",
      "CREATE TABLE commissionFactor (guid VARCHAR(255), revenueType VARCHAR(255), factor FLOAT)",
      "INSERT INTO commissionFactor VALUES ('commissionFactor1', 'saleProduct', 2)",
      "INSERT INTO commissionFactor VALUES ('commissionFactor2', 'service', 1)",
      "INSERT INTO commissionFactor VALUES ('commissionFactor3', 'rental', 0)",

    };

}//CreateDB class



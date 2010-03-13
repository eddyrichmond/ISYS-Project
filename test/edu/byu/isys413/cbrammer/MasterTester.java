/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author chbrammer
 */
public class MasterTester {

    private Cache cache = Cache.getInstance();

    public MasterTester() {
    }


    

    /** Test the Test geta all BO/DAO */
//    @Test
//    public void GetAllTest() throws Exception {
//        CreateDB.main(null);
//        CustomerDAO.getInstance().getAll();
//        EmployeeDAO.getInstance().getAll();
//        PhysicalProductDAO.getInstance().getAll();
//        ConceptualProductDAO.getInstance().getAll();
//        ProductDAO.getInstance().getAll();
//        AreaOfInterestDAO.getInstance().getAll();
//    }
    
    /** Test the Test geta all BO/DAO */
    @Test
    public void TestBatch() throws Exception {
        


        
    }
//
//     /** Test the Trans BO/DAO */
//    @Test
//    public void TransTest() throws Exception {
//        CreateDB.main(null);
//        TransDAO dao = TransDAO.getInstance();
//
//        // Test reading from the cache
//        Trans cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Trans cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Trans update1 = dao.read("trans1");
//        update1.setEmpGuid("fasdklfajel");
//        update1.setDirty(true);
//        update1.save();
//        Trans update2 = dao.read("trans1");
//        assertEquals(update1.getEmpGuid(), update2.getEmpGuid());
//
//        // Test read from the DB
//        Trans cache1 = dao.read("trans2");
//        cache.clear();
//        Trans cache2 = dao.read("trans2");
//    }
//
//
//    /** Test the Employee BO/DAO */
//    @Test
//    public void PhysicalProductTest() throws Exception {
//        CreateDB.main(null);
//        PhysicalProductDAO dao = PhysicalProductDAO.getInstance();
//
//        // Test reading from the cache
//        PhysicalProduct cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        PhysicalProduct cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        PhysicalProduct update1 = dao.read("phys1");
//        update1.setShelfLocation("leftside");
//        update1.setDirty(true);
//        update1.save();
//        PhysicalProduct update2 = dao.read("phys1");
//        assertEquals(update1.getShelfLocation(), update2.getShelfLocation());
//
//        // Test read from the DB
//        cache.clear();
//        PhysicalProduct cache1 = dao.read("phys1");
//        cache.clear();
//        PhysicalProduct cache2 = dao.read("phys1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//    }
//
//
//    /** Test the Employee BO/DAO */
//    @Test
//    public void ConceptualProductTest() throws Exception {
//        CreateDB.main(null);
//        ConceptualProductDAO dao = ConceptualProductDAO.getInstance();
//
//        // Test reading from the cache
//        ConceptualProduct cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        ConceptualProduct cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        ConceptualProduct update1 = dao.read("conc1");
//        update1.setDescription("testerdesc");
//        update1.setDirty(true);
//        update1.save();
//        ConceptualProduct update2 = dao.read("conc1");
//        assertEquals(update1.getDescription(), update2.getDescription());
//
//        // Test read from the DB
//        cache.clear();
//        ConceptualProduct cache1 = dao.read("conc1");
//        cache.clear();
//        ConceptualProduct cache2 = dao.read("conc1");
//        assertEquals(cache1.getId(), cache2.getId());
//    }
//
//
//    /** Test the Employee BO/DAO */
//    @Test
//    public void TestEmployee() throws Exception {
//        CreateDB.main(null);
//        EmployeeDAO dao = EmployeeDAO.getInstance();
//
//        // Test reading from the cache
//        Employee cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Employee cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Employee update1 = dao.read("emp1");
//        update1.setFirstName("ben");
//        update1.setDirty(true);
//        update1.save();
//        Employee update2 = dao.read("emp1");
//        assertEquals(update1.getFirstName(), update2.getFirstName());
//
//        // Test read from the DB
//        Employee cache1 = dao.read("emp2");
//        cache.clear();
//        Employee cache2 = dao.read("emp2");
//        assertEquals(cache1.getId(), cache2.getId());
//    }
//
//
//
//
//    /** Test the Store BO/DAO */
//    @Test
//    public void StoreTest() throws Exception {
//        CreateDB.main(null);
//        StoreDAO dao = StoreDAO.getInstance();
//
//        // Test reading from the cache
//        Store cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Store cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Store update1 = dao.read("store1");
//        update1.setLocation("etdafae sdsd ");
//        update1.setDirty(true);
//        update1.save();
//        Store update2 = dao.read("store1");
//        assertEquals(update1.getLocation(), update2.getLocation());
//
//        // Test read from the DB
//        Store cache1 = dao.read("store2");
//        cache.clear();
//        Store cache2 = dao.read("store2");
//    }
//
//
//    /** Test the Service BO/DAO */
//    @Test
//    public void ServiceTest() throws Exception {
//        CreateDB.main(null);
//        ServiceDAO dao = ServiceDAO.getInstance();
//
//        // Test reading from the cache
//        Service cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Service cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Service update1 = dao.read("s1");
//        update1.setDescription("etdafae sdsd ");
//        update1.setDirty(true);
//        update1.save();
//        Service update2 = dao.read("s1");
//        assertEquals(update1.getDescription(), update2.getDescription());
//
//        // Test read from the DB
//        Service cache1 = dao.read("s2");
//        cache.clear();
//        Service cache2 = dao.read("s2");
//    }
//
//
//     /** Test the TransLine BO/DAO */
//    @Test
//    public void TransLineTest() throws Exception {
//        CreateDB.main(null);
//        TransLineDAO dao = TransLineDAO.getInstance();
//
//        // Test reading from the cache
//        TransLine cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.setTrans(TransDAO.getInstance().create(GUID.generate()));
//
//        TransLine cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getTransGuid(), cacheRead2.getTransGuid());
//
//        // Test the update
//        TransLine update1 = dao.read("line1");
//        update1.setTrans(TransDAO.getInstance().create(GUID.generate()));
//        update1.save();
//        TransLine update2 = dao.read("line1");
//        assertEquals(update1.getTransGuid(), update2.getTransGuid());
//
//        // Test read from the DB
//        TransLine cache1 = dao.read("line2");
//        cache.clear();
//        TransLine cache2 = dao.read("line2");
//        assertEquals(cache1.getId(), cache2.getId());
//
//    }
//
//    /** Test the SaleProduct BO/DAO */
//    @Test
//    public void ProductTest() throws Exception {
//        CreateDB.main(null);
//        ProductDAO dao = ProductDAO.getInstance();
//
//        // Test reading from the cache
//        Product cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Product cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Product update1 = dao.read("conc1");
//        update1.setPrice(23);
//        update1.setDirty(true);
//        update1.save();
//        Product update2 = dao.read("conc1");
//        assertTrue((update1.getPrice() == update2.getPrice()));
//
//        // Test read from the DB
//        cache.clear();
//        Product cache1 = dao.read("conc1");
//        cache.clear();
//        Product cache2 = dao.read("conc1");
//    }
//
//
//    /** Test the SaleProduct BO/DAO */
//    @Test
//    public void SaleProductTest() throws Exception {
//        CreateDB.main(null);
//        SaleProductDAO dao = SaleProductDAO.getInstance();
//
//        // Test reading from the cache
//        SaleProduct cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        SaleProduct cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        SaleProduct update1 = dao.read("sp1");
//        update1.setQuantity(2);
//        update1.setDirty(true);
//        update1.save();
//        SaleProduct update2 = dao.read("sp1");
//        assertEquals(update1.getQuantity(), update2.getQuantity());
//
//        // Test read from the DB
//        SaleProduct cache1 = dao.read("sp2");
//        cache.clear();
//        SaleProduct cache2 = dao.read("sp2");
//    }
//
//
//    /** Test the RevenueSource BO/DAO */
//    @Test
//    public void RevenueSourceTest() throws Exception {
//        CreateDB.main(null);
//        RevenueSourceDAO dao = RevenueSourceDAO.getInstance();
//
//        // Test reading from the cache
//        RevenueSource cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        RevenueSource cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        RevenueSource update1 = dao.read("sp1");
//        update1.setType("12");
//        update1.setDirty(true);
//        update1.save();
//        RevenueSource update2 = dao.read("sp1");
//        assertEquals(update1.getType(), update2.getType());
//
//        // Test read from the DB
//        RevenueSource cache1 = dao.read("sp2");
//        cache.clear();
//        RevenueSource cache2 = dao.read("sp2");
//    }
//
//    /** Test the PaymentTest BO/DAO */
//    @Test
//    public void PaymentTest() throws Exception {
//        CreateDB.main(null);
//        PaymentDAO dao = PaymentDAO.getInstance();
//
//        // Test reading from the cache
//        Payment cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Payment cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Payment update1 = dao.read("pay1");
//        update1.setType("12");
//        update1.setDirty(true);
//        update1.save();
//        Payment update2 = dao.read("pay1");
//        assertEquals(update1.getType(), update2.getType());
//
//        // Test read from the DB
//        Payment cache1 = dao.read("pay2");
//        cache.clear();
//        Payment cache2 = dao.read("pay2");
//    }
//
//
//    /** Test the JournalEntry BO/DAO */
//    @Test
//    public void JournalEntryTest() throws Exception {
//        CreateDB.main(null);
//        JournalEntryDAO dao = JournalEntryDAO.getInstance();
//
//        // Test reading from the cache
//        JournalEntry cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        JournalEntry cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test read from the DB
//        JournalEntry cache1 = dao.read("j2");
//        cache.clear();
//        JournalEntry cache2 = dao.read("j2");
//    }
//
//    /** Test the Customer BO/DAO */
//    @Test
//    public void TestCustomer() throws Exception {
//        CreateDB.main(null);
//        CustomerDAO dao = CustomerDAO.getInstance();
//
//
//
//        // Test the update
//        Customer update1 = dao.read("cust1");
//        update1.setFirstName("ben");
//        update1.save();
//        Customer update2 = dao.read("cust1");
//        assertEquals(update1.getFirstName(), update2.getFirstName());
//
//        // Test read from the DB
//        Customer cache1 = dao.read("cust2");
//        cache.clear();
//        Customer cache2 = dao.read("cust2");
//        assertEquals(cache1.getId(), cache2.getId());
//    }
//
//    /** Test the Accounting BO/DAO */
//    @Test
//    public void TestAccouting() throws Exception {
//        CreateDB.main(null);
//        AccountingDAO dao = AccountingDAO.getInstance();
//
//        // Test reading from the cache
//        Accounting cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Accounting cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Accounting update1 = dao.read("acc1");
//        update1.setDescription("234234");
//        update1.setDirty(true);
//        update1.save();
//        Accounting update2 = dao.read("acc1");
//        assertEquals(update1.getDescription(), update2.getDescription());
//
//        // Test read from the DB
//        Accounting cache1 = dao.read("acc2");
//        cache.clear();
//        Accounting cache2 = dao.read("acc2");
//        assertEquals(cache1.getId(), cache2.getId());
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestCommission() throws Exception {
//        CreateDB.main(null);
//        CommissionDAO dao = CommissionDAO.getInstance();
//
//        // Test reading from the cache
//        Commission cacheRead1 = dao.create(GUID.generate());
//        cacheRead1.save();
//        Commission cacheRead2 = dao.read(cacheRead1.getId());
//        assertEquals(cacheRead1.getId(), cacheRead2.getId());
//
//        // Test the update
//        Commission update1 = dao.read("comm1");
//        update1.setAmount(23);
//        update1.setPaid("1");
//        update1.save();
//        Commission update2 = dao.read("comm1");
//        assertEquals(update1.getPaid(), update2.getPaid());
//
//        // Test read from the DB
//        Commission cache1 = dao.read("comm2");
//        cache.clear();
//        Commission cache2 = dao.read("comm2");
//        assertEquals(cache1.getId(), cache2.getId());
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestConceptualRental() throws Exception {
//        CreateDB.main(null);
//        ConceptualRentalDAO dao = ConceptualRentalDAO.getInstance();
//
//
//        // Test read from the DB
//        ConceptualRental cache1 = dao.read("conc1");
//        cache.clear();
//        ConceptualRental cache2 = dao.read("conc1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//        // Test the update
//        ConceptualRental update1 = dao.read("conc1");
//        update1.setPricePerDay(13.00);
//        update1.save();
//        cache.clear();
//        ConceptualRental update2 = dao.read("conc1");
//        assertEquals(update1.getPricePerDay(), update2.getPricePerDay());
//
//        // Test the update
//        ConceptualRental create1 = dao.create("conc2");
//        create1.setPricePerDay(15.00);
//        create1.save();
//        ConceptualRental create2 = dao.read("conc2");
//        assertEquals(create1.getPricePerDay(), create2.getPricePerDay());
//
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestForRent() throws Exception {
//        CreateDB.main(null);
//        ForRentDAO dao = ForRentDAO.getInstance();
//
//
//        // Test read from the DB
//        ForRent cache1 = dao.read("phys1");
//        cache.clear();
//        ForRent cache2 = dao.read("phys1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//        // Test the update
//        ForRent update1 = dao.read("phys1");
//        update1.setTimesRented(28);
//        update1.save();
//        cache.clear();
//        ForRent update2 = dao.read("phys1");
//        assertEquals(update1.getTimesRented(), update2.getTimesRented());
//
//        // Test the update
//        ForRent create1 = dao.create("phys2");
//        create1.setTimesRented(4);
//        create1.save();
//        cache.clear();
//        ForRent create2 = dao.read("phys2");
//        assertEquals(create1.getTimesRented(), create2.getTimesRented());
//
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestRental() throws Exception {
//        CreateDB.main(null);
//        RentalDAO dao = RentalDAO.getInstance();
//
//
//        // Test read from the DB
//        Rental cache1 = dao.read("r1");
//        cache.clear();
//        Rental cache2 = dao.read("r1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//        // Test the update
//        Rental update1 = dao.read("r1");
//        update1.setAmount(202.00);
//        update1.save();
//        cache.clear();
//        Rental update2 = dao.read("r1");
//        assertEquals(update1.getAmount(), update2.getAmount());
//
//        // Test the update
//        Rental create1 = dao.create("r2");
//        create1.setAmount(2.00);
//        Timestamp myStamp = new Timestamp(new Date().getTime());
//        create1.setDateOut(myStamp);
//        create1.setDateIn(myStamp);
//        create1.setDateDue(myStamp);
//        create1.setForRentGuid("phys1");
//        create1.save();
//        cache.clear();
//        Rental create2 = dao.read("r2");
//        assertEquals(create1.getAmount(), create2.getAmount());
//
//    }
//
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestMembership() throws Exception {
//        CreateDB.main(null);
//        MembershipDAO dao = MembershipDAO.getInstance();
//
//
//        // Test read from the DB
//        Membership cache1 = dao.read("membership1");
//        cache.clear();
//        Membership cache2 = dao.read("membership1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//        // Test the update
//        Membership update1 = dao.read("membership1");
//        update1.setCreditCardNumber("123");
//        update1.save();
//        cache.clear();
//        Membership update2 = dao.read("membership1");
//        assertEquals(update1.getCreditCardNumber(), update2.getCreditCardNumber());
//
//        // Test the update
//        Membership create1 = dao.create("membership2");
//        Timestamp myStamp = new Timestamp(new Date().getTime());
//        create1.setStartDate(myStamp);
//        create1.setExpireDate(myStamp);
//        create1.setCreditCardNumber("123");
//        LinkedList<AreaOfInterest> areasOfInterest = new LinkedList();
//
//        AreaOfInterest a = AreaOfInterestDAO.getInstance().read("areaOfInterest1");
//
//        areasOfInterest.push(a);
//        create1.setAreasOfInterestList(areasOfInterest);
//        create1.save();
//        cache.clear();
//        Membership create2 = dao.read("membership2");
//        assertEquals(create1.getCreditCardNumber(), create2.getCreditCardNumber());
//
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestCommissionFactor() throws Exception {
//        CreateDB.main(null);
//        CommissionFactorDAO dao = CommissionFactorDAO.getInstance();
//
//
//        // Test read from the DB
//        CommissionFactor cache1 = dao.read("commissionFactor1");
//        cache.clear();
//        CommissionFactor cache2 = dao.read("commissionFactor1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//    }
//
//    /** Test the Commission BO/DAO */
//    @Test
//    public void TestAreasOfInterest() throws Exception {
//        CreateDB.main(null);
//        AreaOfInterestDAO dao = AreaOfInterestDAO.getInstance();
//
//
//        // Test read from the DB
//        AreaOfInterest cache1 = dao.read("areaOfInterest1");
//        cache.clear();
//        AreaOfInterest cache2 = dao.read("areaOfInterest1");
//        assertEquals(cache1.getId(), cache2.getId());
//
//    }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cbrammer
 */
public class Trans extends BusinessObject {

    private String empGuid;
    private Employee employee;
    private String storeGuid;
    private Store store;
    private String customerGuid;
    private Customer customer;
    private String paymentGuid;
    private Payment payment;
    private String journalGuid;
    private JournalEntry journal;
    private String commissionGuid;
    private Commission commission;
    private String date;
    private RevenueSource rs;
    private List<TransLine> transList = new LinkedList<TransLine>();

    /** Creates a new instance of this BO */
    public Trans(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        TransDAO.getInstance().save(this);
    }//save

    /**
     * @return the empGuid
     */
    String getEmpGuid() {
        if(employee != null) {
            return employee.getId();
        } else {
            return empGuid;
        }
    }

    /**
     * @param empGuid the empGuid to set
     */
    void setEmpGuid(String empGuid) {
        this.empGuid = empGuid;
        setDirty(true);
    }

    /**
     * @return the storeGuid
     */
    String getStoreGuid() {
        if(store != null) {
            return store.getId();
        } else {
            return storeGuid;
        }
    }

    /**
     * @param storeGuid the storeGuid to set
     */
    void setStoreGuid(String storeGuid) {
        this.storeGuid = storeGuid;
        setDirty(true);
    }

    /**
     * @return the customerGuid
     */
    String getCustomerGuid() {
        if(customer != null) {
            return customer.getId();
        } else {
            return customerGuid;
        }
    }

    /**
     * @param customerGuid the customerGuid to set
     */
    void setCustomerGuid(String customerGuid) {
        this.customerGuid = customerGuid;
        setDirty(true);
    }

    /**
     * @return the paymentGuid
     */
    String getPaymentGuid() {
        if(payment != null) {
            return payment.getId();
        } else {
            return paymentGuid;
        }
    }

    /**
     * @param paymentGuid the paymentGuid to set
     */
    void setPaymentGuid(String paymentGuid) {
        this.paymentGuid = paymentGuid;
        setDirty(true);
    }

    /**
     * @return the journalGuid
     */
    String getJournalGuid() {
        if(journal != null) {
            return journal.getId();
        } else {
            return journalGuid;
        }
    }

    /**
     * @param journalGuid the journalGuid to set
     */
    void setJournalGuid(String journalGuid) {
        this.journalGuid = journalGuid;
        setDirty(true);
    }

    /**
     * @return the commissionGuid
     */
    String getCommissionGuid() {
        if(commission != null) {
            return commission.getId();
        } else {
            return commissionGuid;
        }
    }

    /**
     * @param commissionGuid the commissionGuid to set
     */
    void setCommissionGuid(String commissionGuid) {
        this.commissionGuid = commissionGuid;
        setDirty(true);
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
        setDirty(true);
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        if(employee == null) {
            setEmpGuid(null);
        }
        this.employee = employee;
        setDirty(true);
    }

    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(Store store) {
        if(store == null) {
            setStoreGuid(null);
        }
        this.store = store;
        setDirty(true);
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        if(customer == null) {
            setCustomerGuid(null);
        }
        this.customer = customer;
        setDirty(true);
    }

    /**
     * @return the payment
     */
    public Payment getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(Payment payment) {
        if(payment == null) {
            setPaymentGuid(null);
        }
        this.payment = payment;
        setDirty(true);
    }

    /**
     * @return the journal
     */
    public JournalEntry getJournal() {
        return journal;
    }

    /**
     * @param journal the journal to set
     */
    public void setJournal(JournalEntry journal) {
        if(journal == null) {
            setJournalGuid(null);
        }
        this.journal = journal;
        setDirty(true);
    }

    /**
     * @return the commission
     */
    public Commission getCommission() {
        return commission;
    }

    /**
     * @param commission the commission to set
     */
    public void setCommission(Commission commission) {
        if(commission == null) {
            setCommissionGuid(null);
        }
        this.commission = commission;
        setDirty(true);
    }

    /**
     * @return the rs
     */
    public RevenueSource getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(RevenueSource rs) {
        this.rs = rs;
    }

    /**
     * @return the transList
     */
    public List<TransLine> getTransList() {
        return transList;
    }

    /**
     * @param transList the transList to set
     */
    public void setTransList(List<TransLine> tlist) {
        this.transList = tlist;
    }

   

}

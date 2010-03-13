/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Commission extends BusinessObject {
    
    private double amount;
    private String paid;

    /** Creates a new instance of this BO */
    public Commission(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        CommissionDAO.getInstance().save(this);
    }//save

    /**
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        setDirty(true);
        this.amount = amount;
    }

    /**
     * @return the paid
     */
    public String getPaid() {
        return paid;
    }

    /**
     * @param paid the paid to set
     */
    public void setPaid(String paid) {
        setDirty(true);
        this.paid = paid;
    }

    

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Payment extends BusinessObject {
    private double amount;
    private double change;
    private String type;

    /** Creates a new instance of this BO */
    public Payment(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        PaymentDAO.getInstance().save(this);
    }//save


    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
        setDirty(true);
    }

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
        this.amount = amount;
        setDirty(true);
    }

    /**
     * @return the change
     */
    public double getChange() {
        return change;
    }

    /**
     * @param change the change to set
     */
    public void setChange(double change) {
        this.change = change;
        setDirty(true);
    }

    

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 * @author Garrett
 */
public class Rental extends RevenueSource {


    private Timestamp dateOut = null;
    private Timestamp dateIn = null;
    private Timestamp dateDue = null;
    private Double amount = 0.0;
    private ForRent forRent;
    private String forRentGuid;


    /** Creates a new instance of this BO */
    public Rental(String id) {
        // call the BusinessObject constructor
        super(id);
    }

    /** Saves this object using its DAO */
    @Override
    public void save() throws DataException {
        RentalDAO.getInstance().save(this);
    }//save


    @Override
    public void save(Connection conn) throws Exception {
        RentalDAO.getInstance().save(this, conn);
        super.save(conn);
    }

    /**
     * @return the dateOut
     */
    public Timestamp getDateOut() {
        return dateOut;
    }

    /**
     * @param dateOut the dateOut to set
     */
    public void setDateOut(Timestamp dateOut) {
        this.dateOut = dateOut;
        setDirty(true);
    }

    /**
     * @return the dateIn
     */
    public Timestamp getDateIn() {
        return dateIn;
    }

    /**
     * @param dateIn the dateIn to set
     */
    public void setDateIn(Timestamp dateIn) {
        this.dateIn = dateIn;
        setDirty(true);
    }

    /**
     * @return the dateDue
     */
    public Timestamp getDateDue() {
        return dateDue;
    }

    /**
     * @param dateDue the dateDue to set
     */
    public void setDateDue(Timestamp dateDue) {
        this.dateDue = dateDue;
        setDirty(true);
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
        setDirty(true);
    }

    /**
     * @return the transLineGuid
     */
    String getForRentGuid() {
        if(forRent != null) {
            return forRent.getId();
        } else {
            return forRentGuid;
        }
    }

    /**
     * @param transLineGuid the transLineGuid to set
     */
    void setForRentGuid(String forRentGuid) {
        this.forRentGuid = forRentGuid;
        setDirty(true);
    }

    /**
     * @return the transLine
     */
    public ForRent getForRent() {
        return forRent;
    }

    /**
     * @param transLine the transLine to set
     */
    public void setForRent(ForRent forRent) {
        if(forRent == null) {
            setForRentGuid(null);
        }
        this.forRent = forRent;
        setDirty(true);
    }

    @Override
    public String toString() {
        return this.getForRent().getConceptualProduct().getName();
    }

    public Double getPrice(){
        return this.getPrice();
    }
}

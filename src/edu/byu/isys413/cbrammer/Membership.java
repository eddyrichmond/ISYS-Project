/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;


import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class Membership extends BusinessObject {

    private String creditCardNumber;
    private Timestamp startDate;
    private Timestamp expireDate;
    private List<AreaOfInterest> areasOfInterestList = new LinkedList();

    /** Creates a new instance of this BO */
    public Membership (String id) {
        // call the BusinessObject constructor
        super(id);
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        MembershipDAO.getInstance().save(this);
    }//save


    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
        setDirty(true);
    }

    /**
     * @return the startDate
     */
    public Timestamp getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        setDirty(true);
    }

    /**
     * @return the expireDate
     */
    public Timestamp getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate the expireDate to set
     */
    public void setExpireDate(Timestamp expireDate) {
        this.expireDate = expireDate;
        setDirty(true);
    }

    /**
     * @return the areasOfInterestList
     */
    public List<AreaOfInterest> getAreasOfInterestList() {
        return areasOfInterestList;
    }

    /**
     * @param areasOfInterestList the areasOfInterestList to set
     */
    public void setAreasOfInterestList(List<AreaOfInterest> areasOfInterestList) {
        this.areasOfInterestList = areasOfInterestList;
        setDirty(true);
    }



}

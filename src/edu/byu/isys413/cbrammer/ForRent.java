/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author Garrett
 */
public class ForRent extends PhysicalProduct {

    private int timesRented = 0;
    private boolean isRented = false;


    /** Creates a new instance of this BO */
    public ForRent (String id) {
        // call the BusinessObject constructor
        super(id);
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        ForRentDAO.getInstance().save(this);
    }//save

    /**
     * @return the timesRented
     */
    public int getTimesRented() {
        return timesRented;
    }

    /**
     * @param timesRented the timesRented to set
     */
    public void setTimesRented(int timesRented) {
        this.timesRented = timesRented;
        setDirty(true);
    }

    /**
     * @return the isRented
     */
    public boolean isIsRented() {
        return isRented;
    }

    /**
     * @param isRented the isRented to set
     */
    public void setIsRented(boolean isRented) {
        this.isRented = isRented;
        setDirty(true);
    }




}

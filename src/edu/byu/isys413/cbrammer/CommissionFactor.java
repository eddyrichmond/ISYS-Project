/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author Garrett
 */
public class CommissionFactor extends BusinessObject {



    private String revenueType;
    private Double factor;

    /** Creates a new instance of this BO */
    public CommissionFactor(String id) {
        // call the BusinessObject constructor
        super(id);
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        CommissionFactorDAO.getInstance().save(this);
    }//save

    /**
     * @return the revenueType
     */
    public String getRevenueType() {
        return revenueType;
    }

    /**
     * @param revenueType the revenueType to set
     */
    public void setRevenueType(String revenueType) {
        this.revenueType = revenueType;
        setDirty(true);
    }

    /**
     * @return the factor
     */
    public Double getFactor() {
        return factor;
    }

    /**
     * @param factor the factor to set
     */
    public void setFactor(Double factor) {
        this.factor = factor;
        setDirty(true);
    }



}

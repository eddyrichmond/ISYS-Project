/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author Garrett
 */
public class ConceptualRental extends ConceptualProduct {


        private Double pricePerDay = 0.0;
        private Double replacementPrice = 0.0;




    /** Creates a new instance of this BO */
       public ConceptualRental(String id) {
        // call the BusinessObject constructor
        super(id);
    }




        /** Saves this object using its DAO */
    public void save() throws DataException {
        ConceptualRentalDAO.getInstance().save(this);
    }//save



    /////////////////////////////////////////////////////////////////////////////////////////////////////////
       //getters and setters

    /**
     * @return the pricePerDay
     */
    public Double getPricePerDay() {
        return pricePerDay;
    }

    /**
     * @param pricePerDay the pricePerDay to set
     */
    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
        setDirty(true);
    }

    /**
     * @return the replacementPrice
     */
    public Double getReplacementPrice() {
        return replacementPrice;
    }

    /**
     * @param replacementPrice the replacementPrice to set
     */
    public void setReplacementPrice(Double replacementPrice) {
        this.replacementPrice = replacementPrice;
        setDirty(true);
    }

}

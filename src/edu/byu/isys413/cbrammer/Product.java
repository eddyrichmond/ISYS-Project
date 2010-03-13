/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Product extends BusinessObject {
    
    private double price;

    /** Creates a new instance of this BO */
    public Product(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        ProductDAO.getInstance().save(this);
    }//save

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
        setDirty(true);
    }

    public String getUPC(){

        throw new UnsupportedOperationException();
    }


    public String getDesc(){

        throw new UnsupportedOperationException();
    }


     public double getCogs(){


       throw new UnsupportedOperationException();
    }

     public String toString() {
        return null;
     }

     public void populate(Product p) {
         setPrice(p.getPrice());
     }



}

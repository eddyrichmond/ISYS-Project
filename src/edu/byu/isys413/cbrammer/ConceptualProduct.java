/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class ConceptualProduct extends Product {
    private String name;
    private String description;
    private String manufacturer;
    private Double averageCost = 0.0;
    private String upc;
    
    /** Creates a new instance of this BO */
    public ConceptualProduct(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        ConceptualProductDAO.getInstance().save(this);
    }//save

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        setDirty(true);
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        setDirty(true);
        this.description = description;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        setDirty(true);
        this.manufacturer = manufacturer;
    }

    /**
     * @return the averageCost
     */
    public Double getAverageCost() {
        return averageCost;
    }

    /**
     * @param averageCost the averageCost to set
     */
    public void setAverageCost(Double averageCost) {
        setDirty(true);
        this.averageCost = averageCost;
    }

    /**
     * @return the upc
     */
    public String getUpc() {
        return upc;
    }

    /**
     * @param upc the upc to set
     */
    public void setUpc(String upc) {
        setDirty(true);
        this.upc = upc;
    }


      @Override
     public String getUPC(){

        return this.getUpc();
       // throw new UnsupportedOperationException();
    }


    @Override
    public String getDesc(){

        return this.getDescription();
       // throw new UnsupportedOperationException();
    }

    @Override
    public double getCogs(){
        return this.getAverageCost();
    }

    @Override
    public String toString() {
        return getName();
    }



}

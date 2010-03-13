/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class PhysicalProduct extends Product {
    private String conceptualProductGuid;
    private ConceptualProduct conceptualProduct;
    private String serial;
    private String shelfLocation;
    private String datePurchased;
    private Double cost = 0.0;
    
    /** Creates a new instance of this BO */
    public PhysicalProduct(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        PhysicalProductDAO.getInstance().save(this);
    }//save

    /**
     * @return the conceptualProductGuid
     */
    public String getConceptualProductGuid() {
        if(conceptualProduct != null) {
            return conceptualProduct.getId();
        } else {
            return conceptualProductGuid;
        }
    }

    /**
     * @param conceptualProductGuid the conceptualProductGuid to set
     */
    public void setConceptualProductGuid(String conceptualProductGuid) {
        setDirty(true);
        this.conceptualProductGuid = conceptualProductGuid;
    }

    /**
     * @return the conceptualProduct
     */
    public ConceptualProduct getConceptualProduct() {
        return conceptualProduct;
    }

    /**
     * @param conceptualProduct the conceptualProduct to set
     */
    public void setConceptualProduct(ConceptualProduct conceptualProduct) {
        if(conceptualProduct == null) {
            setConceptualProductGuid(null);
        }
        setDirty(true);
        this.conceptualProduct = conceptualProduct;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        setDirty(true);
        this.serial = serial;
    }

    /**
     * @return the shelfLocation
     */
    public String getShelfLocation() {
        return shelfLocation;
    }

    /**
     * @param shelfLocation the shelfLocation to set
     */
    public void setShelfLocation(String shelfLocation) {
        setDirty(true);
        this.shelfLocation = shelfLocation;
    }

    /**
     * @return the datePurchased
     */
    public String getDatePurchased() {
        return datePurchased;
    }

    /**
     * @param datePurchased the datePurchased to set
     */
    public void setDatePurchased(String datePurchased) {
        setDirty(true);
        this.datePurchased = datePurchased;
    }

    /**
     * @return the cost
     */
    public Double getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(Double cost) {
        setDirty(true);
        this.cost = cost;
    }


    @Override
     public String getUPC(){

        return this.getSerial();
       // throw new UnsupportedOperationException();
    }


    @Override
    public String getDesc(){

        return this.getConceptualProduct().getDescription();
       // throw new UnsupportedOperationException();
    }

    @Override
     public double getCogs(){

    return this.getCost();
       //throw new UnsupportedOperationException();
    }

    public String toString() {
        return getConceptualProduct().toString();
    }

    public void populate(PhysicalProduct pp) {
        super.populate(pp);

        setConceptualProduct(pp.getConceptualProduct());
        setConceptualProductGuid(pp.getConceptualProductGuid());
        setSerial(pp.getSerial());
        setShelfLocation(pp.getShelfLocation());
        setCost(pp.getCost());
        setDatePurchased(pp.getDatePurchased());
    }
}

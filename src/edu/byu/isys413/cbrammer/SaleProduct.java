/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;


import java.sql.Connection;
/**
 *
 * @author cbrammer
 */
public class SaleProduct extends RevenueSource {
    
    private int quantity = 0;
    private String productGuid;
    private Product product;

    /** Creates a new instance of this BO */
    public SaleProduct(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    @Override
    public void save() throws DataException {
        SaleProductDAO.getInstance().save(this);
    }//save


    @Override
    public void save(Connection conn) throws Exception {
        Boolean _dirtyFlag = isDirty();
        Boolean _inDB = isObjectAlreadyInDB();

        SaleProductDAO.getInstance().save(this, conn);

        setDirty(_dirtyFlag);
        setObjectAlreadyInDB(_inDB);
        super.save(conn);
    }

    /**
     * @return the quantity
     */
    @Override
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        setDirty(true);
    }

    /**
     * @return the productGuid
     */
    public String getProductGuid() {
        if(product != null) {
            return product.getId();
        } else {
            return productGuid;
        }
    }

    /**
     * @param productGuid the productGuid to set
     */
    public void setProductGuid(String productGuid) {
        this.productGuid = productGuid;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        if(product == null) {
            setProductGuid(null);
        }
        this.product = product;
    }

    

    /////////////////////////////////////////////////////
    //
    //              METHODS
    //
    //////////////////////////////////////////////////////


    @Override
     public String getUPC(){

         return this.getProduct().getUPC();
         //throw new UnsupportedOperationException();
    }


    @Override
    public String getDesc(){

        return this.getProduct().getDesc();
        //throw new UnsupportedOperationException();
    }

    
    @Override
    public Double getPrice(){

     
     return this.getProduct().getPrice();

    }

    @Override
     public double getCost(){

    return this.getProduct().getCogs();
       //throw new UnsupportedOperationException();
    }

    public String toString() {
        return getProduct().toString();
    }




}

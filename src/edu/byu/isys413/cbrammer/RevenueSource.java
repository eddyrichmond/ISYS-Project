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
public class RevenueSource extends BusinessObject {
    private String transLineGuid;
    private TransLine transLine;
    private String commGuid;
    private Commission comm;
    private String type;

    /** Creates a new instance of this BO */
    public RevenueSource(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        RevenueSourceDAO.getInstance().save(this);
    }//save

    public void save(Connection conn) throws Exception {
        RevenueSourceDAO.getInstance().save(this, conn);
    }
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
     * @return the transLineGuid
     */
    String getTransLineGuid() {
        if(transLine != null) {
            return transLine.getId();
        } else {
            return transLineGuid;
        }
    }

    /**
     * @param transLineGuid the transLineGuid to set
     */
    void setTransLineGuid(String transLineGuid) {
        this.transLineGuid = transLineGuid;
        setDirty(true);
    }

    /**
     * @return the transLine
     */
    public TransLine getTransLine() {
        return transLine;
    }

    /**
     * @param transLine the transLine to set
     */
    public void setTransLine(TransLine transLine) {
        if(transLine == null) {
            setTransLineGuid(null);
        }
        this.transLine = transLine;
        setDirty(true);
    }

    /**
     * @return the transLineGuid
     */
    String getCommGuid() {
        if(comm != null) {
            return comm.getId();
        } else {
            return commGuid;
        }
    }

    /**
     * @param transLineGuid the transLineGuid to set
     */
    void setCommGuid(String commGuid) {
        this.commGuid = commGuid;
        setDirty(true);
    }

    /**
     * @return the transLine
     */
    public Commission getComm() {
        return comm;
    }

    /**
     * @param transLine the transLine to set
     */
    public void setComm(Commission comm) {
        if(comm == null) {
            setCommGuid(null);
        }
        this.comm = comm;
        setDirty(true);
    }



//////////////////////////////////////////////////
//
//              METHODS
//
///////////////////////////////////////////////////

    public String getUPC(){

     throw new UnsupportedOperationException();
    }


    public String getDesc(){

     throw new UnsupportedOperationException();
    }

    public Double getPrice(){

     throw new UnsupportedOperationException();
    }

     public int getQuantity(){

     throw new UnsupportedOperationException();
    }

      public double getCost(){

     throw new UnsupportedOperationException();
    }

}

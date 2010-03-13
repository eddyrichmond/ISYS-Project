/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class TransLine extends BusinessObject {
    
    private String transGuid = "";
    private Trans trans;
    private RevenueSource rs;
    private String revenueSourceGuid;
    /** Creates a new instance of this BO */
    public TransLine(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        TransLineDAO.getInstance().save(this);
    }//save

    /**
     * @return the transGuid
     */
    String getTransGuid() {
        if(trans != null) {
            return trans.getId();
        } else {
            return transGuid;
        }
    }

    String getRevenueSourceGuid() {
        if(rs != null) {
            return rs.getId();
        } else {
            return revenueSourceGuid;
        }
    }

    /**
     * @param transGuid the transGuid to set
     */
    void setRevenueSourceGuid(String revenueSourceGuid) {
        this.revenueSourceGuid = revenueSourceGuid;
        setDirty(true);
    }

    /**
     * @param transGuid the transGuid to set
     */
    void setTransGuid(String transGuid) {
        this.transGuid = transGuid;
        setDirty(true);
    }

    /**
     * @return the trans
     */
    public Trans getTrans() {
        return trans;
    }

    /**
     * @param trans the trans to set
     */
    public void setTrans(Trans trans) {
        if(trans == null) {
            setTransGuid(null);
        }
        this.trans = trans;
        setDirty(true);
    }

    /**
     * @return the rs
     */
    public RevenueSource getRs() {
        return rs;
    }

    /**
     * @param rs the rs to set
     */
    public void setRs(RevenueSource rs) {
        if(rs == null) {
            setRevenueSourceGuid(null);
        }
        this.rs = rs;
        setDirty(true);
    }

   

    
    

}

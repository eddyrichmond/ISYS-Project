/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Store extends BusinessObject {

    private String location;
    private String managersGuid;
    private Employee manager;
    private String address;
    private String phone;
    
    /** Creates a new instance of this BO */
    public Store(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        StoreDAO.getInstance().save(this);
    }//save

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
        setDirty(true);
    }

    /**
     * @return the managersGuid
     */
    String getManagersGuid() {
        if(manager != null) {
            return manager.getId();
        } else {
            return managersGuid;
        }
    }

    /**
     * @param managersGuid the managersGuid to set
     */
    void setManagersGuid(String managersGuid) {
        this.managersGuid = managersGuid;
        setDirty(true);
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
        setDirty(true);
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
        setDirty(true);
    }

    /**
     * @return the manager
     */
    public Employee getManager() {
        return manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(Employee manager) {
        if(manager == null) {
            setManagersGuid(null);
        }
        this.manager = manager;
        setDirty(true);
    }

    

}

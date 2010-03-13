/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Employee extends BusinessObject {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String storeGuid;
    private Store store;
    private String hireDate;
    private String phone;
    private Double salary = 0.0;
    private Double commissionRate = 0.0;
    
    /** Creates a new instance of this BO */
    public Employee(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        EmployeeDAO.getInstance().save(this);
    }//save

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
        setDirty(true);
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
        setDirty(true);
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        setDirty(true);
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
        setDirty(true);
    }

    /**
     * @return the storeGuid
     */
    String getStoreGuid() {
        if(store != null) {
            return store.getId();
        } else {
            return storeGuid;
        }
    }

    /**
     * @param storeGuid the storeGuid to set
     */
    void setStoreGuid(String storeGuid) {
        this.storeGuid = storeGuid;
        setDirty(true);
    }

    /**
     * @return the hireDate
     */
    public String getHireDate() {
        return hireDate;
    }

    /**
     * @param hireDate the hireDate to set
     */
    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
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
     * @return the salary
     */
    public Double getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(Double salary) {
        this.salary = salary;
        setDirty(true);
    }

    /**
     * @return the commissionRate
     */
    public Double getCommissionRate() {
        return commissionRate;
    }

    /**
     * @param commissionRate the commissionRate to set
     */
    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
        setDirty(true);
    }

    /**
     * @return the store
     */
    public Store getStore() {
        return store;
    }

    /**
     * @param store the store to set
     */
    public void setStore(Store store) {
        if(store == null) {
            setStoreGuid(null);
        }
        this.store = store;
        setDirty(true);
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }

}

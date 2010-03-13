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
public class Service extends RevenueSource {

    private String employeeGuid;
    private Employee employee;
    private Double amount = 0.0;
    private Double laborHours = 0.0;
    private String Description;
    private String dateStarted;
    private String dateCompleted;
    private String datePickedUp;
    private String barcode;

    
    /** Creates a new instance of this BO */
    public Service(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    @Override
    public void save() throws DataException {
        ServiceDAO.getInstance().save(this);
    }//save

    @Override
    public void save(Connection conn) throws Exception {
        ServiceDAO.getInstance().save(this, conn);
        super.save(conn);
    }

    /**
     * @return the employeeGuid
     */
    String getEmployeeGuid() {
        if(employee != null) {
            return employee.getId();
        } else {
            return employeeGuid;
        }
    }

    /**
     * @param employeeGuid the employeeGuid to set
     */
    void setEmployeeGuid(String employeeGuid) {
        this.employeeGuid = employeeGuid;
        setDirty(true);
    }

    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
        setDirty(true);
    }

    /**
     * @return the laborHours
     */
    public Double getLaborHours() {
        return laborHours;
    }

    /**
     * @param laborHours the laborHours to set
     */
    public void setLaborHours(Double laborHours) {
        this.laborHours = laborHours;
        setDirty(true);
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
        setDirty(true);
    }

    /**
     * @return the dateStarted
     */
    public String getDateStarted() {
        return dateStarted;
    }

    /**
     * @param dateStarted the dateStarted to set
     */
    public void setDateStarted(String dateStarted) {
        this.dateStarted = dateStarted;
        setDirty(true);
    }

    /**
     * @return the dateCompleted
     */
    public String getDateCompleted() {
        return dateCompleted;
    }

    /**
     * @param dateCompleted the dateCompleted to set
     */
    public void setDateCompleted(String dateCompleted) {
        this.dateCompleted = dateCompleted;
        setDirty(true);
    }

    /**
     * @return the datePickedUp
     */
    public String getDatePickedUp() {
        return datePickedUp;
    }

    /**
     * @param datePickedUp the datePickedUp to set
     */
    public void setDatePickedUp(String datePickedUp) {
        this.datePickedUp = datePickedUp;
        setDirty(true);
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        if(employee == null) {
            setEmployeeGuid(null);
        }
        this.employee = employee;
        setDirty(true);
    }
    /**
     * @return the barcode
     */
    public String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    @Override
     public Double getPrice(){

     return this.getAmount();
    }

    @Override
     public String getUPC(){

     return this.getBarcode();
    }


    @Override
    public String getDesc(){

     return this.getDescription();
    }


    @Override
     public int getQuantity(){

     return 1;
    }

    @Override
     public double getCost(){

    return 0;
    }

    @Override
    public String toString() {
        return getDesc();
    }



}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import javax.swing.DefaultListModel;



/**
 *
 * @author chbrammer
 */
public class Model {


    private Employee employee;
    DefaultListModel savedCustomersModel = new DefaultListModel();

    private static Model instance = null;

    /**
     * Exists as protected to block out of class creation
     */
    protected Model() {}

    /**
     * Singleton provider
     * @return
     */
    public static Model getInstance() {
        //  Check to see if we have alreay instanciated this, if not, create it
        if(instance == null)
            instance = new Model();

        //Return our class
        return instance;
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
        this.employee = employee;
    }

    public void populateCustomers() {
        List<Customer> custList = null;
        try {
            custList = CustomerDAO.getInstance().getAll();
            for (Customer c: custList) {
                savedCustomersModel.addElement(c);
            }

        } catch (DataException ex) {
            
        }//end for
    }
}



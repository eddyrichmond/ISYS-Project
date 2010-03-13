/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author cbrammer
 */
public class JournalEntry extends BusinessObject {
    
    private List<Accounting> AccountingList = new LinkedList<Accounting>();

    /** Creates a new instance of this BO */
    public JournalEntry(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        JournalEntryDAO.getInstance().save(this);
    }//save

    /**
     * @return the AccountingList
     */
    public List<Accounting> getAccountingList() {
        return AccountingList;
    }

    /**
     * @param AccountingList the AccountingList to set
     */
    public void setAccountingList(List<Accounting> AccountingList) {
        this.AccountingList = AccountingList;
    }

    

}

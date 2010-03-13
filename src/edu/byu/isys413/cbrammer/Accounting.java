/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Accounting extends BusinessObject {
    private String journalGuid;
    private JournalEntry journalEntry;
    private String type;
    private String description;
    private double amount;

    /** Creates a new instance of this BO */
    public Accounting(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        AccountingDAO.getInstance().save(this);
    }//save

    /**
     * @param journalGuid the journalGuid to set
     */
    void setJournalGuid(String journalGuid) {
        setDirty(true);
        this.journalGuid = journalGuid;
    }

    /**
     * @param journalGuid the journalGuid to set
     */
    String getJournalGuid() {
        if(journalEntry != null) {
            return journalEntry.getId();
        } else {
            return journalGuid;
        }
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
        setDirty(true);
        this.type = type;
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
     * @return the amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        setDirty(true);
        this.amount = amount;
    }

    /**
     * @return the journalEntry
     */
    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    /**
     * @param journalEntry the journalEntry to set
     */
    public void setJournalEntry(JournalEntry journalEntry) {
        if(journalEntry == null) {
            setJournalGuid(null);
        }
        setDirty(true);
        this.journalEntry = journalEntry;
    }
}

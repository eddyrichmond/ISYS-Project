/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Garrett
 */
public class AreaOfInterest extends BusinessObject {


    private String description;
    private List<Membership> membershipsList = new LinkedList();


    /** Creates a new instance of this BO */
    public AreaOfInterest (String id) {
        // call the BusinessObject constructor
        super(id);
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        AreaOfInterestDAO.getInstance().save(this);
    }//save

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
        this.description = description;
        setDirty(true);
    }

    /**
     * @return the membershipsList
     */
    public List<Membership> getMembershipsList() {
        return membershipsList;
    }

    /**
     * @param membershipsList the membershipsList to set
     */
    public void setMembershipsList(List<Membership> membershipsList) {
        this.membershipsList = membershipsList;
        setDirty(true);
    }


}

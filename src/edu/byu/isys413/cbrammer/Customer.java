/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

/**
 *
 * @author cbrammer
 */
public class Customer extends BusinessObject {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String membershipGuid;
    private Membership membership;



    
    /** Creates a new instance of this BO */
    public Customer(String id) {
        // call the BusinessObject constructor
        super(id); 
    }

    /** Saves this object using its DAO */
    public void save() throws DataException {
        CustomerDAO.getInstance().save(this);
    }//save



    /**
     * @return the conceptualProductGuid
     */
    public String getMembershipGuid() {
        if(membership != null) {
            return membership.getId();
        } else {
            return membershipGuid;
        }
    }

    /**
     * @param conceptualProductGuid the conceptualProductGuid to set
     */
    public void setMembershipGuid(String membershipGuid) {
        setDirty(true);
        this.membershipGuid = membershipGuid;
    }

    /**
     * @return the conceptualProduct
     */
    public Membership getMembership() {
        return membership;
    }

    /**
     * @param conceptualProduct the conceptualProduct to set
     */
    public void setMembership(Membership membership) {
        if(membership == null) {
            setMembershipGuid(null);
        }
        setDirty(true);
        this.membership = membership;
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
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return getLastName() + ", " + getFirstName();
    }

}

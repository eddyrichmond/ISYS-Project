/////////////////////////////////////////////////////////////////
///   This file is part of the ISys 413 starter code for
///   the ISys Core at Brigham Young University.  Students
///   may use the code as part of the 413 course in their
///   milestones following this one, but no permission is given
///   to use this code is any other way.  Since we will likely
///   use this case again in a future year, please DO NOT post
///   the code to a web site, share it with others, or pass
///   it on in any way.


package edu.byu.isys413.cbrammer;

/**
 * The super-class of all business objects in the program.
 *
 * @author Conan C. Albrecht
 * @version 2010-02-01
 */
public abstract class BusinessObject {
    
  boolean objectAlreadyInDB = false;
  protected boolean dirty = true;
  protected String id = null;
  
  /** Creates a new instance of BusinessObject */
  public BusinessObject(String id) {
    this.id = id;
  }//constructor

  /** Returns the guid of this BO */
  public String getId() {
      return id;
  }//getID

  /** Returns whether the object is in the DB or not */
  boolean isObjectAlreadyInDB() {
    return objectAlreadyInDB;
  }//isObjectAlreadyInDB

  /** 
   * Sets whether the object is already in the DB.  This method
   * is called ONLY from the DAO responsible for this object.
   */
  void setObjectAlreadyInDB(boolean objectAlreadyInDB) {
    this.objectAlreadyInDB = objectAlreadyInDB;
  }//setObjectAlreadyInDB
  
  /** Returns whether the object needs saving or not */
  public boolean isDirty() {
    return dirty;
  }//isDirty
  
  /** 
   * Sets whether the object needs saving or not.  This method
   * is called ONLY from the DAO responsible for this object.
   */
  void setDirty(boolean dirty) {
    this.dirty = dirty;
  }//setDirty

  /** Just a convenience method that calls setDirty(true) */
  void setDirty() {
    setDirty(true);
  }//setDirty


  /** Saves this object using its DAO */
  public abstract void save() throws DataException;

  
}//class

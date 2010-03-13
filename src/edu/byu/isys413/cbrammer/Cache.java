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

import java.util.*;

/**
 * @author Conan C. Albrecht
 * @version 2010-02-01
 *
 * A decorator for a map that automatically times out objects that
 * are not accessed within the given amount of time.
 * <p>
 * The map is smart enough to recognize if the underlying map it is decorating
 * has changed and add those items to its cache.
 * <p>
 * <b>The memory limit has not yet been programmed!</b> It is a change
 * that just hasn't been implemented yet.  Sorry!
 */
public class Cache extends Thread implements Map {

  /** Default amount of time for an object to timeout and be removed (30 minutes) */
  public static final long DEFAULT_TIMEOUT_MILLIS = 30 * 60 * 1000;
  /** Default memory limit percentage (90%) */
  public static final float DEFAULT_MEMORY_LIMIT = 0.9f;
  /** The maximum sleep time between checks for old items/memory limit */
  public static final long MAXIMUM_SLEEP_TIME = 15 * 60 * 1000;

  protected long timeout = DEFAULT_TIMEOUT_MILLIS;
  protected float memory = DEFAULT_MEMORY_LIMIT;
  protected Map map = null;
  protected Map accessTimes = null;
  protected boolean running = false;

  /** Singelton instance of this class */
  private static Cache instance = null;
  
  
  /** Constructor with a default timout of 30 minutes and no memory limit (a value of 100%) */
  private Cache(Map map) {
    this(map, DEFAULT_TIMEOUT_MILLIS, DEFAULT_MEMORY_LIMIT);
  }//constructor

  /** Constructor with a specified timeout period and memory percentage. The
   *  memory limit should be between 0.0 and 1.0.  A memory limit of 1+ means no
   *  memory limit. */
  private Cache(Map map, long timeoutInMillis, float memoryLimit) {
    this.map = map;
    this.timeout = timeoutInMillis;
    this.memory = memoryLimit;

    // create a map of the same time as this map for the access times
    // this provides for the same underlying strengths and weaknesses as the map
    // the user sends in
    try {
      Class c = map.getClass();
      accessTimes = (Map)c.newInstance();
    }catch (Exception e) {
      System.err.println("Error creating access times map of the same type as the given map.  Using a HashMap instead.");
      accessTimes = new HashMap();
    }//try

    // start our thread that watches for timed out objects
    this.setDaemon(true);
    this.start();
  }//constructor

  
  
  /** Returns the single instance of the class */
  public static synchronized Cache getInstance() {
    if (instance == null) {
      instance = new Cache(new TreeMap());
    }
    return instance;
  }
  
  
  /** Main loop that checks for inactive objects */
  public void run() {
    running = true;
    long sleepTime = Math.min(MAXIMUM_SLEEP_TIME, timeout);
    while (running) {
      try {
        // first sleep a bit (we sleep a maximum of 15 minutes)
        sleep(sleepTime);

        // go through the map and check to see if anything is too old
        synchronized(this) { // we must synchronize to ensure we don't access the accesstimes map during check
          List removeKeys = new LinkedList();
          long old = System.currentTimeMillis() - timeout;
          for (Iterator iter = accessTimes.keySet().iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            Date val = (Date)accessTimes.get(key);
            if (val.getTime() <= old) {
              removeKeys.add(key);

            }else if (!map.containsKey(key)) {
              removeKeys.add(key);

            }//if
          }//for iter

          // actually delete the ones that should be removed
          // this is required because if we remove while using the iterator, we get a ConcurrentModificationException
          for (Iterator iter = removeKeys.iterator(); iter.hasNext(); ) {
            remove(iter.next());
          }//for

          // go through the map and see if the user added anything in it that we don't have in the access map
          List addKeys = new LinkedList();
          for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            Object key = iter.next();
            if (!accessTimes.containsKey(key)) {
              addKeys.add(key);
            }//if
          }//for iter

          // actually add the ones that should be added
          // this is required because if we add while using the iterator, we get a ConcurrentModificationException
          for (Iterator iter = addKeys.iterator(); iter.hasNext(); ) {
            touch(iter.next());
          }//for


        }//synchronized

      }catch (InterruptedException e) {
        // no arg.  this one is ok
      }catch (Exception e) {
        e.printStackTrace();
      }//try
    }//while
  }//run

  /** Stops the timedmap explicitly.  It cannot be restarted (use a new one instead). */
  public void cleanStop() {
    this.running = false;
  }//stop

  /** Updates the access time for the given key.  This is done automatically
   *  by the regular Map methods, but it can also be done explicitly here. */
  public synchronized void touch(Object key) {
    accessTimes.put(key, new Date());
  }//touch

  /** Touches all keys in a given collection.  This is done automatically by the regular
   *  Map methods, but it can also be done explicitly here. */
  public synchronized void touch(Collection keys) {
    Date now = new Date();
    for (Iterator iter = keys.iterator(); iter.hasNext(); ) {
      accessTimes.put(iter.next(), now);
    }//for iter
  }//touch

  /** Clears the map */
  public void clear() {
    accessTimes.clear();
    map.clear();
  }//clear

  /** Returns whether the map contains the given key */
  public boolean containsKey(Object key) {
    if (map.containsKey(key)) {
      touch(key);
      return true;
    }//if
    return false;
  }//containsKey

  /** Returns whether the map contains the given value */
  public boolean containsValue(Object value) {
    for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
      Object key = iter.next();
      Object val = map.get(key);
      if (val.equals(value)) {
        touch(key);
        return true;
      }//if
    }//for iter
    return false;
  }//containsValue

  /** Returns a set view of the entries. This method updates the access times for
   *  all elements in the map to now. */
  public Set entrySet() {
    accessTimes.clear(); // we're redoing everything so we might as well
    touch(map.keySet());
    return map.entrySet();
  }//entrySet

  /** Returns whether this map is equal to another one */
  public boolean equals(Object  o) {
    return map.equals(o);
  }//equals

  /** Returns the specified value mapped to this key */
  public Object get(Object key) {
    containsKey(key); //touches if it exists
    return map.get(key);
  }//get

  /** Returns the hash code for this map */
  public int hashCode() {
    return super.hashCode();
  }//hasCode

  /** Returns whether the map is empty or not */
  public boolean isEmpty() {
    return map.isEmpty();
  }//isEmpty

  /** Returns a set of keys. Does not update any access times. */
  public Set keySet() {
    return map.keySet();
  }//keySet
  
  
  /** Places a key/value mapping in the map */
  public Object put(Object key, Object value) {
    touch(key);
    return map.put(key, value);
  }//put

  /** Places all the items in the map */
  public void putAll(Map m) {
    touch(m.keySet());
    map.putAll(m);
  }//putAll

  /** Removes an item from the map */
  public Object remove(Object key) {
    accessTimes.remove(key);
    return map.remove(key);
  }//remove

  /** Returns the number of items in the map */
  public int size() {
    return map.size();
  }//size

  /** Returns a collection of the values in this map.  Updates the access times of all values. */
  public Collection values() {
    accessTimes.clear();
    touch(map.keySet());
    return map.values();
  }//values

}//TimedMap
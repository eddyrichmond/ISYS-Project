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
 * Version 2.7
 *
 * A Globally-Unique Identifier.  This is a transcode of my Python
 * class at http://aspn.activestate.com/ASPN/Cookbook/Python/Recipe/163604
 * as of January 20, 2006 (GUID.py version 2.7).
 *
 * @author Conan C. Albrecht
 * @version 2010-02-01
 */
public class GUID
{

 private final static String zeros = "0000000000000000000000000000000000000000";
 private final static long MAX_COUNTER = Long.parseLong("fffffffe", 16); 
 private static String iphex = null;
 private static long counter = 0;
 private static long firstcounter = 0;
 private static long lasttime = 0;

 /** Generates a new globally unique id, based upon current system time and system IP address */
 public static synchronized String generate () {
   StringBuffer newGUID = new StringBuffer();

   // do we need to wait for the next millisecond (are we out of counters?)
   long now = System.currentTimeMillis();
   while (lasttime == now && counter == firstcounter) {
     try {
       Thread.sleep(10);
     }catch (InterruptedException e) {
       // just skip over the interruption and test again
     }
     now = System.currentTimeMillis();
   }
   
   // time part
   newGUID.append(pad(Long.toHexString(now), 16));
   
   // counter part
   if (lasttime != now) {
     firstcounter = (long)(1 + (Math.random() * (MAX_COUNTER - 1)));
     counter = firstcounter;
   }
   counter++;
   if (counter > MAX_COUNTER) {
     counter = 0;
   }
   lasttime = now;
   newGUID.append(pad(Long.toHexString(counter), 8));
   
   // ip part
   if (iphex == null) {  // only need to calculate it once
     iphex = "";
     try {
       byte ip[] = java.net.InetAddress.getLocalHost().getAddress();

       for (int i = 0; i < ip.length; i++) {
         int n = ip[i];

         if (ip[i] < 0) {
           n += 256;
         } //if
         iphex += pad(Integer.toHexString(n), 4);
       throw new Exception();
       } //for i
     }
     catch (Exception e) {
       iphex = "0010"; // start in 10. range
       for (int i = 1; i < 4; i++) {
         int num = (int)(1 + (Math.random() * (254)));
         iphex += pad(Integer.toHexString(num), 4);
       }
     } //try
   }
   newGUID.append(iphex);
   
   // return it
   return newGUID.toString();
 } //getGUID


 /** Pads a string with zeros on the left side */
 private static String pad(String s, int len) {
   if (len <= s.length()) {
     return s;
   } else {
     return zeros.substring(0, len-s.length()) + s;
   }//if
 }//pad


 /** Debugging only */
 public static void main(String args[]) throws Exception {
   for (int i = 0; i < 10; i++) {
     System.out.println(GUID.generate());
   }
 }//main

}//class

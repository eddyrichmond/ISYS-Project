/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 *
 * @author chbrammer
 */
public class Batch {

    public static void main(String[] args) throws Exception {
        Batch b = new Batch();
        b.doBatch();
    }//main method

    public void doBatch() {

        // Clear the DB
        try {
            CreateDB.main(null);
        } catch (Exception ex) {
            Logger.getLogger(Batch.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Get NOW
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date myDate = cal.getTime();
        Timestamp now = new Timestamp(myDate.getTime());

        // Get all late rentals
        try {
            List<Rental> rentals = RentalDAO.getInstance().loadLateRentals(now);

            for (Rental r:rentals) {
                processLateRental(r, now);
            }
        } catch (DataException ex) {
            Logger.getLogger(Batch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processLateRental(Rental r, Timestamp t) {
        String[] to={"danmerrell@gmail.com"};
        String[] cc={"cbrammer@gmail.com"};
        String[] bcc={"cbrammer@gmail.com"};
//        SendEmail.sendMail("cbrammer@gmail.com","+sNq0UbJ!D-GV5A","smtp.gmail.com","465","true", "true",true,"javax.net.ssl.SSLSocketFactory","false",to,cc,bcc,"Congrats","You just bought your rental product");
    }

}

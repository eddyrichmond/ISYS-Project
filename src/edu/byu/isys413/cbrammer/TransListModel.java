/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.byu.isys413.cbrammer;

import java.util.LinkedList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Garrett
 */
public class TransListModel extends AbstractTableModel {

    private List<TransLine> tlist = new LinkedList<TransLine>();

    public TransListModel(List<TransLine> tlist ){


        this.tlist = tlist;

    }


    public int getRowCount() {

        return tlist.size();

       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getColumnCount() {

        return 6;

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getValueAt(int rIndex, int cIndex) {

        if (cIndex==0){
            return tlist.get(rIndex).getRs().getUPC();
        }else

        if (cIndex==1){
            return tlist.get(rIndex).getRs().getDesc();
        }else

        if (cIndex==2){
            return tlist.get(rIndex).getRs().getQuantity();
        }else

        if (cIndex==3){
            return tlist.get(rIndex).getRs().getType();
        }else

        if (cIndex==4){
            return tlist.get(rIndex).getRs().getPrice();
        }else

        if (cIndex==5){
            return tlist.get(rIndex).getRs().getPrice() * tlist.get(rIndex).getRs().getQuantity()  ;
        }else

        return "somethings wrong";



       // return "asd";

        //throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public String getColumnName (int column){

        switch (column){

            case 0 : return "Barcode";
            case 1 : return "Description";
            case 2 : return "Quantity";
            case 3 : return "Type";
            case 4 : return "Unit Price";
            case 5 : return "Extended Price";
            default:return "What the crap happened!?";

        }//end switch


    }
    
    





}//end class

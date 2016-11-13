package quickjournal.bhupendrashekhawat.me.android.quickjournal.util;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bhupendra Shekhawat on 13/11/16.
 */

public class DateHelper {

    public static long convertDateToEpoch(int year, int monthOfYear, int dayOfMonth){

        long epoch = 0;
        String str = year+"-"+monthOfYear+"-"+dayOfMonth;   // UTC

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date datenew = null;
        try {
            datenew = df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        epoch= datenew.getTime()/1000;   //to seconds

        return epoch;
    }


    //get month string

    public static String getDisplayDate(int year, int monthOfYear, int dayOfMonth) {

        String month = new DateFormatSymbols().getMonths()[monthOfYear];

        String output = month+" "+dayOfMonth;
        return  output;
    }
}

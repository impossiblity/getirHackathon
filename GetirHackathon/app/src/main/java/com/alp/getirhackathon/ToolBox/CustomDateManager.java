package com.alp.getirhackathon.ToolBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by AlparslanSelçuk on 25.03.2017.
 */

public class CustomDateManager {
    private static Calendar c;
    private static SimpleDateFormat formatter;
    private static SimpleDateFormat timeFormatter;

    private static void initialize() {
        c = Calendar.getInstance(Locale.getDefault());
        formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    public static String getDateWellFormedString(int increment) {
        initialize();
        c.add(Calendar.DATE, increment);
        return formatter.format(c.getTime());
    }

    public static String getTimeWellFormedString(int increment){
        initialize();
        c.add(Calendar.HOUR, increment);
        return timeFormatter.format(c.getTime());
    }

    public static String printDifference(int different){

        int elapsedDays = different / 60 /24;
        different = different % (60 * 24);

        long elapsedHours = (different) / (60);
        different = different % 60;

        long elapsedMinutes = different;


        if (elapsedDays != 0) {
            if (elapsedDays < 7) {
                return String.valueOf(elapsedDays) + " gün";
            } else if (elapsedDays >= 7 && elapsedDays <= 30)
                return String.valueOf(elapsedDays / 7) + " hafta";
            else
                return String.valueOf((elapsedDays / 30)) + " ay";
        } else if (elapsedHours != 0)
            return String.valueOf(elapsedHours) + " saat";
        else if(elapsedMinutes != 0)
            return String.valueOf(elapsedMinutes) + " dakika";
        else
            return null;
    }

}

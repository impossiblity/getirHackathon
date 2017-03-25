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
        formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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

}

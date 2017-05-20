package cz.matyapav.todoapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.matyapav.todoapp.R;
import cz.matyapav.todoapp.todo.model.TodoDay;
import cz.matyapav.todoapp.todo.util.enums.TodoPriority;
import cz.matyapav.todoapp.todo.model.Cathegory;
import cz.matyapav.todoapp.todo.model.Todo;

/**
 * @author Pavel Matyáš (matyapav@fel.cvut.cz).
 * @since 1.0.0..
 */
public class Utils {

    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    public static Date parseDate(String dateString){
        try {
            return dateFormatter.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Unable to parse date from string - "+dateString);
            e.printStackTrace();
        }
        return null;
    }

    public static int getValueSpinnerPosition(Spinner spinner, String value) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }

    public static int parseHourFromString(String time){
        return Integer.parseInt(time.substring(0,2));
    }

    public static int parseMinutesFromString(String time){
        return Integer.parseInt(time.substring(3,5));
    }

    public static Date getOnlyDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}

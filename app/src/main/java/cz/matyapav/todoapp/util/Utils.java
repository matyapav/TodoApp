package cz.matyapav.todoapp.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cz.matyapav.todoapp.todo.util.enums.SupportedLanguages;

/**
 * Util methods used in applicaiton
 */
public class Utils {

    /**
     * Date formatter - formats date in dd.MM.yyyy format
     */
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    /**
     * Gets color by its id
     * @param context
     * @param id
     * @return corresponding color
     */
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Parses date from string in dd.MM.yyyy format
     * @param dateString
     * @return parsed Date or null if it cannot be parsed from given string
     */
    public static Date parseDate(String dateString){
        try {
            return dateFormatter.parse(dateString);
        } catch (ParseException e) {
            System.err.println("Unable to parse date from string - "+dateString);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets position of given value in specified spinner
     * @param spinner
     * @param value
     * @return position of given value in spinner
     */
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

    /**
     * Parses hours from time string (expects time in HH:MM format)
     * @param time
     * @return hours as integer
     */
    public static int parseHourFromString(String time){
        return Integer.parseInt(time.substring(0,2));
    }

    /**
     * Parses minutes from time string (expects time in HH:MM format)
     * @param time
     * @return minutes as integer
     */
    public static int parseMinutesFromString(String time){
        return Integer.parseInt(time.substring(3,5));
    }

    public static String[] getSupportedLanguagesAsArray(){
        int numberOfLangs = SupportedLanguages.values().length;
        String[] result = new String[numberOfLangs];
        for (int i = 0; i < numberOfLangs; i++) {
            result[i] = SupportedLanguages.values()[i].getLangName();
        }
        return result;
    }

    /**
     * Gets name of month for Czech localization
     * @param month
     * @return Month name in Czech language
     */
    public static String getMonthStandaloneName(int month) {
        switch (month+1){
            case 1:
                return "Leden";
            case 2:
                return "Únor";
            case 3:
                return "Březen";
            case 4:
                return "Duben";
            case 5:
                return "Květen";
            case 6:
                return "Červen";
            case 7:
                return "Červenec";
            case 8:
                return "Srpen";
            case 9:
                return "Zaří";
            case 10:
                return "Říjen";
            case 11:
                return "Listopad";
            case 12:
                return "Prosinec";
            default:
                return null;
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    public static boolean isDeviceOnline(Activity context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}

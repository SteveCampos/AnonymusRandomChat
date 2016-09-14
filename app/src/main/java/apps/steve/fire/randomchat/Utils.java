package apps.steve.fire.randomchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import apps.steve.fire.randomchat.model.Emisor;
import apps.steve.fire.randomchat.model.Looking;

/**
 * Created by Steve on 3/07/2016.
 */

public class Utils {

    public static final String TAG = Utils.class.getSimpleName();

    public static String capitalize(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static boolean isBetween(int x, int lower, int upper) {

        if (upper - lower <= 2)
        {
            Log.d(TAG, "LOS RANGOS SON MUY PEQUEÑOS <= 2");
            upper = upper + 2;
            lower = lower - 2;
        }

        return lower <= x && x <= upper;
    }

    public static boolean isInRange(int inputMin, int inputMax, int rangeLower, int rangeUpper){
        return inputMin < rangeLower && inputMax > rangeUpper;
    }

    public static String removeTwoLastChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        return str.substring(0,str.length()-2);
    }

    public static String getAndroidID(Context context){
            String android_id;
            android_id = get_android_id(context);
            if (TextUtils.isEmpty(android_id)){
                Log.d(TAG, "android_id is empty");
                android_id = get_android_id(context);
            }
        return android_id;
    }
    private static String get_android_id(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    //MEJORAR ESTO
    public static String calculateAgeGroup(String[][] groups, Constants.Genero genero, int edadMin, int edadMax){


        ArrayList<String> listGroupAge = getListGroupAge(groups, genero, edadMin, edadMax);
        String ageGroupList = "";
        for (int i=0; i< listGroupAge.size(); i++)
        {
            ageGroupList += listGroupAge.get(i) + ", ";
        }
        ageGroupList = removeTwoLastChar(ageGroupList);
        return ageGroupList;
    }

    private static ArrayList<String> getListGroupAge(String [][] groups, Constants.Genero genero, int edadMin, int edadMax){
        ArrayList<String> listGroupAge = new ArrayList<>();
        //GRUPO 1
        String ageGroup = getGroup(groups, genero, edadMin, edadMax, 0, 24);
        if (!TextUtils.isEmpty(ageGroup)){
            listGroupAge.add(ageGroup);
        }
        //grupo 2
        String ageGroup2 = getGroup(groups, genero, edadMin, edadMax, 25, 35);
        if (!TextUtils.isEmpty(ageGroup2)){
            listGroupAge.add(ageGroup2);
        }

        String ageGroup3 = getGroup(groups, genero, edadMin, edadMax, 36, 45);
        if (!TextUtils.isEmpty(ageGroup3)){
            listGroupAge.add(ageGroup3);
        }

        String ageGroup4 = getGroup(groups, genero, edadMin, edadMax, 46, 60);
        if (!TextUtils.isEmpty(ageGroup4)){
            listGroupAge.add(ageGroup4);
        }
        return listGroupAge;
    }

    public static String getGroup(String[][] groups, Constants.Genero genero, int edadMin, int edadMax, int rangeLower, int rangeUpper){
        int pertenece = 0;
        String group = "";
        //EL RANGO ESTÁ DENTRO DE LA EDAD DEL GRUPO 1
        if (isInRange(edadMin, edadMax, rangeLower, rangeUpper))
        {
            Log.d(TAG, "IS IN RANGE");
            pertenece++;
        }else{
            //EL RANGO ESTÁ DENTRO DE LA EDAD DEL GRUPO 1
            if (isBetween(edadMin, rangeLower, rangeUpper))
            {
                //EL MÍNIMO ESTÁ DENTRO DEL RANGO
                Log.d(TAG, "receptorEdadMin");
                pertenece++;
            }else if (isBetween(edadMax, rangeLower, rangeUpper)){
                //EL MÁXIMO ESTÁ DENTRO DEL RANGO
                Log.d(TAG, "receptorEdadMax");
                pertenece++;
            }
        }

        Log.d(TAG, "PERTENECE COUNT : " + pertenece);
        if (pertenece>0){
            group = getAgeGroup(groups, genero, rangeUpper);
        }
        return group;
    }

    public static String getAgeGroup(String[][] groups, Constants.Genero genero, int rangeUpper){
        //AGE GROUP DEFAULT
        //String ageGroup = context.getString(R.string.boy_age_group_1);
        String ageGroup = "";
        switch (genero){
            case CHICO:
                switch (rangeUpper)
                {
                    case 24:
                        ageGroup = groups[0][0];
                        break;
                    case 35:
                        ageGroup = groups[0][1];
                        break;
                    case 45:
                        ageGroup = groups[0][2];
                        break;
                    case 60:
                        ageGroup = groups[0][3];
                        break;
                    default:
                        break;
                }
                break;
            case CHICA:

                switch (rangeUpper)
                {
                    case 24:
                        ageGroup = groups[1][0];
                        break;
                    case 35:
                        ageGroup = groups[1][1];
                        break;
                    case 45:
                        ageGroup = groups[1][2];
                        break;
                    case 60:
                        ageGroup = groups[1][3];
                        break;
                    default:
                        break;
                }
                break;
            default:
                Log.d(TAG, "WHAT THE HELL GÉNERO IS THIS? RECEPTOR");
                break;
        }
        return ageGroup;
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static String  calculateLastConnection(Date startDate, Date endDate, Context context){

        String lastConnection = "";
        String period = "";
        long rest= 0;
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        if (different<0){
            different = -different;
        }

        Log.d(TAG, "startDate : " + startDate);
        Log.d(TAG, "endDate : "+ endDate);
        Log.d(TAG, "different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;
        long monthInMilli = daysInMilli * 30;

        if (different <= minutesInMilli){
            rest = (different / secondsInMilli);
            period = context.getString(R.string.time_seconds);
        }else if (different <= hoursInMilli){
            rest = (different / minutesInMilli);
            period = context.getString(R.string.time_minutes);
        }else if(different <= daysInMilli){
            rest = (different / hoursInMilli);
            period = context.getString(R.string.time_hours);
        }else if (different <= monthInMilli){
            rest = (different / daysInMilli);
            period = context.getString(R.string.time_days);
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
            return formatter.format(startDate);
        }
        Log.d(TAG,"%d resto: "+rest+", %s periodo: "+ period);

        lastConnection = String.format(Locale.getDefault(), context.getString(R.string.last_connection), rest, period);

        /*
        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        Log.d(TAG,
                String.format("%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds));*/
        return lastConnection;

    }

    public static int getDrawable(String gender){
        int id = R.drawable.naturesvg;
        /*
        switch (Constants.Genero.valueOf(gender)){
            case CHICO:
                id = R.drawable.boy_1;
            break;
            case CHICA:
                id =  R.drawable.girl_2;
            break;
            default:
                break;
        }*/
        return id;
    }

    public static void saveNoReaded(Context context, int count){
        int noReaded = getNoReaded(context);
        noReaded += count;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("no_readed", noReaded);
        editor.apply();
    }

    public static void saveEmisor(Context context, Emisor emisor){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("emisorEdad", emisor.getEdad());
        editor.putString("emisorGenero", emisor.getGenero());
        editor.putString("emisorKeyDevice", emisor.getKeyDevice());
        editor.putInt("lookingEdadMin", emisor.getLooking().getEdadMin());
        editor.putInt("lookingEdadMax", emisor.getLooking().getEdadMax());
        editor.putString("lookingGenero", emisor.getLooking().getGenero());
        editor.apply();
    }

    public static Emisor getEmisor(Context context){
        Emisor emisor = new Emisor();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        emisor.setEdad(preferences.getInt("emisorEdad",18));
        emisor.setGenero(preferences.getString("emisorGenero", "CHICO"));
        emisor.setKeyDevice(preferences.getString("emisorKeyDevice", "no"));
        Looking looking = new Looking(
                preferences.getInt("lookingEdadMin",18),
                preferences.getInt("lookingGenero",48),
                preferences.getString("lookingGenero","CHICO")
        );
        emisor.setLooking(looking);
        return emisor;
    }

    public static int getNoReaded(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("no_readed", 0);
    }

}

package library;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by Sander on 21-10-2014.
 */
public class PrefUtils {
    public static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    public static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__";
    private static GsonBuilder gsonb;
    private static Gson gson;

    public PrefUtils() {

    }


    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        gsonb = new GsonBuilder();
        gson = gsonb.create();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void saveToPrefs(Context context, String key, List list) {
        gsonb = new GsonBuilder();
        gson = gsonb.create();
        String value = gson.toJson(list);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }


    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Offer[] getAllOffersFromPrefs(Context context, String key, List defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String value = sharedPrefs.getString(key, null);
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            Offer[] list = gson.fromJson(value, Offer[].class);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Offer getOfferFromPrefs(Context context, String key, int offerID) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            String value = sharedPrefs.getString(key, null);
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            Offer[] list = gson.fromJson(value, Offer[].class);
            for (Offer o : list){
                if (o.getID() == offerID){
                    return o;
                }

            }
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
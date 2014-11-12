package library;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4klant.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sander on 7-11-2014.
 */
public class DatabaseHandler {
    private static DatabaseHandler _instance = null;
    private static DatabaseFunctions db;

   public DatabaseHandler() {

    }

    //create instance
    private synchronized static void createInstance (final Context context) {
        if (_instance == null) _instance = new DatabaseHandler();
        db  = new DatabaseFunctions(context);
    }

    //get instance
    public static DatabaseHandler getInstance (final Context context) {
        if (_instance == null) createInstance (context);
        return _instance;
    }


    //update offers
    public void updateOffers(final Context context){
        ServerRequestHandler.getAllOffers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List<Offer> list = Offer.fromJSON(jsonArray);
                PrefUtils.saveToPrefs(context, context.getString(R.string.PREFS_OFFERS), list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    //update beacons
    public void updateBeacons(){
        ServerRequestHandler.getAllBeacons(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                db.resetBeaconTable();
                Log.d("Beacon UPdate", jsonArray.toString());
                for (int i = 0; i<jsonArray.length();i++ ){
                    try {
                        int id = (Integer)jsonArray.getJSONObject(i).getInt("beaconID");
                        int product = (Integer)jsonArray.getJSONObject(i).getInt("productID");
                        int offer = (Integer)jsonArray.getJSONObject(i).getInt("offerID");
                        int minor = (Integer)jsonArray.getJSONObject(i).getInt("minor");
                        int major = (Integer)jsonArray.getJSONObject(i).getInt("major");
                        db.addBeacon(id,product,offer,minor,major);
                        Log.d("Beacon Update", "SUCCES");
                    }
                    catch (Exception e){
                        Log.d("exception", e.toString());
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }

    public ArrayList<ibeacon> getAllBeacons(){
        return db.getAllBeacons();
    }
}

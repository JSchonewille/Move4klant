package library;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

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

    //categories FUNCTIONS
    public void updateCategories(){
        ServerRequestHandler.getAllCategories(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                List<Category> catList = db.getLikedCategories();
                db.resetCategory();
                List<Category> list = Category.fromJSON(jsonArray);

                Log.d("Categorie Update", jsonArray.toString());
                for (Category cat : list) {
                    try {
                        int liked = 0;
                        for (Category c : catList) {
                            if (c.getName().equals(cat.getName()))
                            {liked = 1;}
                        }
                        db.addCategory(cat.getID(), cat.getName(), liked);

                    } catch (Exception e) {
                        Log.d("exception", e.toString());
                    }
                    Log.d("Categorie Update", "SUCCES");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
    }
    public ArrayList<Category> getAllCategories(){
        return db.getAllCategories();
    }
    public List<Category> getLikedCategories(){
        return db.getLikedCategories();
    }
    public void saveLikedCategories(List<Category> likes){
        List<Category> list = db.getAllCategories();
        db.resetCategory();
        for (Category cat : list) {
            try {
                int liked = 0;
                for (Category c : likes) {
                    if (c.getName().equals(cat.getName()))
                    {liked = 1;}
                }
                db.addCategory(cat.getID(), cat.getName(), liked);

            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
            Log.d("Offer Update", "SUCCES");

        }
    }

    //OFFER FUNCTIONS
    public void updateOffers(){
        ServerRequestHandler.getAllOffers(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                db.resetOfferTable();
                List<Offer> list = Offer.fromJSON(jsonArray);

                Log.d("Offer Update", jsonArray.toString());
                for (int i = 0; i<list.size();i++ ){
                    try {
                        db.addOffer(list.get(i).getID(), list.get(i).getCategoryID(), list.get(i).getDescription(), list.get(i).getImage());
                        Log.d("Offer Update", "SUCCES");
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
    public Offer getOfferById(int offerID){return db.getOfferByID(offerID);}
    public ArrayList<Offer> getOfferByLikedCategories (){return db.getOffersByLikedCategories();}

    //BEACON FUNCTIONS
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
    public ibeacon getBeaconByMinor(int minorID){
      return db.getBeaconByMinor(minorID);
    }

    //CHECKIN AND CHECKOUT FUNCTIONS
    public void checkinout(int userid){}
}

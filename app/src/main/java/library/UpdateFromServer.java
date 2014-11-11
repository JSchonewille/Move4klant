package library;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.jeff.move4klant.R;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Sander on 7-11-2014.
 */
public class UpdateFromServer {
    private static UpdateFromServer _instance = null;

   public UpdateFromServer() {

    }

    //create instance
    private synchronized static void createInstance () {
        if (_instance == null) _instance = new UpdateFromServer ();
    }

    //get instance
    public static UpdateFromServer getInstance () {
        if (_instance == null) createInstance ();
        return _instance;
    }

    //update offers
    public void updateOffers(final Context context){
        OfferRequest.getAllOffers(new Response.Listener<JSONArray>() {
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
}

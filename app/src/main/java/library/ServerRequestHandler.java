package library;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.jeff.move4klant.RequestController;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sander on 11-11-2014.
 */
public class ServerRequestHandler {

    public static void getAllCategories(Response.Listener<JSONArray> l, Response.ErrorListener el){
        String URL = Config.CATEGORYURL;

        // HashMap<String, String> params  = new HashMap<String, String>();

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance().addToRequestQueue(req);

    }

    public static void getAllOffers(Response.Listener<JSONArray> l, Response.ErrorListener el){
        String URL = Config.GETALLOFFERS;

        //HashMap<String, String> params  = new HashMap<String, String>();

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);

        RequestController.getInstance().addToRequestQueue(req);
    }


    public static void getAllBeacons(Response.Listener<JSONArray> l, Response.ErrorListener el){
        String URL = Config.GETALLBEACONS;
        //HashMap<String, String> params  = new HashMap<String, String>();
        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);
        RequestController.getInstance().addToRequestQueue(req);
    }

    public static void getAllProducts(Response.Listener<JSONArray> l, Response.ErrorListener el){
        String URL = Config.GETALLPRODUCTS;
        JsonArrayRequest req = new JsonArrayRequest(URL, l, el);
        RequestController.getInstance().addToRequestQueue(req);
    }

    public static void uploadLikes(Response.Listener<JSONArray> l, Response.ErrorListener el, final int customerID,  final Integer[]categories){
        String URL = Config.EDITLIKESURL;
        JsonArrayRequest req = new JsonArrayRequest(URL, l, el){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params  = new HashMap<String, String>();
                params.put("customerID", Integer.toString(customerID));
                params.put("categories", categories.toString());
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(req);
    }

    public static void uploadUserImage(Response.Listener<JSONArray> l, Response.ErrorListener el, final int customerID,  final byte[] image){
        String URL = Config.UPLOADIMAGE;
        Log.e("user", customerID + "");
        Log.e("Image", encodeImage(image));
        JsonArrayRequest req = new JsonArrayRequest(URL, l, el)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params  = new HashMap<String, String>();
                params.put("customerID", Integer.toString(customerID));
                params.put("file", encodeImage(image));
                return params;
            }
            @Override
            public int getMethod() {
                return Method.POST;
            }

        };

        RequestController.getInstance().addToRequestQueue(req);
    }


    public static void checkinout(Response.Listener<JSONArray> l, Response.ErrorListener el, final int customerID){
        String URL = Config.EDITLIKESURL;

        JsonArrayRequest req = new JsonArrayRequest(URL, l, el){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params  = new HashMap<String, String>();
                params.put("customerID", Integer.toString(customerID));

                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(req);
    }

    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, 1);
    }



}

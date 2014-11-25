package library;


import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sander on 11-11-2014.
 */
public class ServerRequestHandler {

    //Requests to get information from server
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

    //Likes server requests
    public static void uploadLikes(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID,  final Integer[]categories){
        String URL = Config.EDITLIKESURL;

        HashMap<String, String> params  = new HashMap<String, String>();
        params.put("customerID", Integer.toString(customerID));
        params.put("categories", Arrays.toString(categories));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params),l, el);

        RequestController.getInstance().addToRequestQueue(req);
    }
    public static void getLikes(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID){
        String URL = Config.GETLIKESURL;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL, new JSONObject(params), l, el);

        RequestController.getInstance().addToRequestQueue(request);
    }
    public static void getLikesFromServer(Response.Listener<JSONArray> l, Response.ErrorListener el, final int customerID){
        String URL = Config.GETLIKESURL;



        JsonArrayRequest request = new JsonArrayRequest(URL, l, el) {
            @Override
            public Map<String, String> getParams()  {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("customerID", String.valueOf(customerID));
                return params;
            }
        };

        RequestController.getInstance().addToRequestQueue(request);
    }

    //User server requests
    public static void uploadUserImage(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID,  final byte[] image){
        String URL = Config.UPLOADIMAGE;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));
        params.put("image", encodeImage(image));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance().addToRequestQueue(request);
    }
    public static void uploadUserEditedInfo(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID, final String name, final String lastname, final String email){
        String URL = Config.EDITUSER;
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));
        params.put("name",name);
        params.put("email",email);
        params.put("lastname",lastname);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);
        //JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance().addToRequestQueue(request);
    }

    public static void checkinout(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID){
        String URL = Config.CHECKINURL;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance().addToRequestQueue(request);
    }
    public static void checkinstatus(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID){
        String URL = Config.CHECKINGSTATUS;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), l, el);

        RequestController.getInstance().addToRequestQueue(request);
    }
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, 1);
    }
}


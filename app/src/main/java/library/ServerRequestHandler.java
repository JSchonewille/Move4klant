package library;


import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jeff.move4klant.RequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
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

    public static void uploadUserImage(Response.Listener<JSONObject> l, Response.ErrorListener el, final int customerID,  final byte[] image){
        String URL = Config.UPLOADIMAGE;
        Log.e("user",String.valueOf(customerID));
        Log.e("image", encodeImage(image));
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("customerID", String.valueOf(customerID));
        params.put("image", encodeImage(image));

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

class CustomRequest extends Request<JSONObject> {

    private Response.Listener<JSONObject> listener;
    private String params;

    public CustomRequest(String url, String params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    public CustomRequest(int method, String url, String params, Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, com.android.volley.toolbox.HttpHeaderParser.parseCharset(response.headers));

            return Response.success(new JSONObject(jsonString), com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new com.android.volley.ParseError(e));
        } catch (JSONException je) {
            return Response.error(new com.android.volley.ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }
}

class CustomJsonArrayRequest extends com.android.volley.toolbox.JsonRequest<org.json.JSONArray> {
    public CustomJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, null, listener, errorListener);
     /*   super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(),
                listener, errorListener);
    }
    protected com.android.volley.Response<org.json.JSONObject> parseNetworkResponse(com.android.volley.NetworkResponse response) {
        try {
            String jsonString = new String(response.data, com.android.volley.toolbox.HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new  com.android.volley.ParseError(e));
        } catch (JSONException je) {
            return Response.error(new  com.android.volley.ParseError(je));
        }*/
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
        try {
            String jsonString =
                    new String(networkResponse.data, com.android.volley.toolbox.HttpHeaderParser.parseCharset(networkResponse.headers));
            return Response.success(new JSONArray(jsonString),
                    com.android.volley.toolbox.HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new com.android.volley.ParseError(e));
        } catch (JSONException je) {
            return Response.error(new com.android.volley.ParseError(je));
        }
    }
}

package library;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.jeff.move4klant.RequestController;

import org.json.JSONArray;

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

}

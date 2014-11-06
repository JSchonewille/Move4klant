package library;

/**
 * Created by Sander on 8-10-2014.
 */
import android.content.Context;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class APIFunctions {
    private static APIFunctions _instance = null;

    private JSONParser jsonParser;

    /***************
     *EXPLANATION API URLs
     ***************
     * Add here the URL of your PHP API, it should look like this:
     * private static String your_API_URL = "http://example.com/your_API/";
     **/

    private static String checkinoutURL = "http://ibeacontest.herobo.com/checkinout.php";
    private static String getcategoriesURL = "http://ibeacontest.herobo.com/getcategories.php";
    private static String editlikesURL = "http://ibeacontest.herobo.com/editlikes.php";
    private static String getlikesURL = "http://ibeacontest.herobo.com/getlikes.php";

    private static String loginURL = "http://supermonsieurbalzaqsmashbros.comxa.com/login_api/";
    private static String registerURL = "http://supermonsieurbalzaqsmashbros.comxa.com/login_api/";
    private static String forpassURL = "http://supermonsieurbalzaqsmashbros.comxa.com/login_api/";
    private static String chgpassURL = "http://supermonsieurbalzaqsmashbros.comxa.com/login_api/";


    //constructor
    private APIFunctions(){
        jsonParser = new JSONParser();
    }

    //create instance
    private synchronized static void createInstance () {
        if (_instance == null) _instance = new APIFunctions ();
    }

    //get instance
    public static APIFunctions getInstance () {
        if (_instance == null) createInstance ();
        return _instance;
    }

    /***************
     *EXPLANATION
     ***************
     * To call your API and retrieve the JSON object,
     * create a new function like this:
     *
     *         public JSONObject yourFunction(String yourParameter <<as many as you want>>){
     *
     *              List params = new ArrayList();
     *              params.add(new BasicNameValuePair("yourKey", yourParameter));
     *
     * // now, the jsonParser will retrieve the JSON object from your API URL, and will send the parameters with the POST request
     *
     *              JSONObject json = jsonParser.getJSONFromUrl(your_API_URL, params);
     *              return json;
     *          }
     *
     *
     * To use this in your own class,
     * just call the function you created in this document like this:
     *
     *      APIFunctions someName = APIFunctions.getInstance();
     *      JSONObject json = someName.yourFunction(yourParameters);
     *
     * Now you can use the information the API has returned
     **/



    /**
     * Function to Login
     **/
    public JSONObject loginUser(String email, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "login"));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        return json;
    }
    /**
     * Function to change password
     **/
    public JSONObject chgPass(String newpas, String email){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "chgpass"));
        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(chgpassURL, params);
        return json;
    }
    /**
     * Function to reset the password
     **/
    public JSONObject forPass(String forgotpassword){
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "forpass"));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(forpassURL, params);
        return json;
    }
    /**
     * Function to  Register
     **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password){
        // Building Parameters
        List params = new ArrayList();
        params.add(new BasicNameValuePair("tag", "register"));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }

    /**
     * function to check the user in or out
     *
     */

    public JSONObject checkInOut(String costumerID)
    {
        List params = new ArrayList();
        params.add(new BasicNameValuePair("costumerID",costumerID));
        JSONObject json = jsonParser.getJSONFromUrl(checkinoutURL,params);
        return json;
    }

    public JSONObject getCategories()
    {
        JSONObject json = jsonParser.getJSONFromUrl(getcategoriesURL);
        return json;
    }

    public JSONObject editLikes(ArrayList<Integer> idlist ,String costumerID)
    {
        List params = new ArrayList();

        params.add(new BasicNameValuePair("categories",idlist.toString()));
        params.add(new BasicNameValuePair("costumerID",costumerID));
        JSONObject json = jsonParser.getJSONFromUrl(editlikesURL,params);
        return json;
    }

    public JSONObject getLikes(String costumerID)
    {
        List params = new ArrayList();

        params.add(new BasicNameValuePair("costumerID", costumerID));
        JSONObject json = jsonParser.getJSONFromUrl(getlikesURL,params);
        return json;
    }















}
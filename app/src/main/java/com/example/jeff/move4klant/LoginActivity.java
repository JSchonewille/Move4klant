package com.example.jeff.move4klant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import library.APIFunctions;
import library.DatabaseHandler;
import library.PrefUtils;

public class LoginActivity extends Activity {

    Button btnLogin;
    EditText inputEmail;
    EditText inputPassword;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "customerID";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_PROFILEIMAGE = "profileImage";
    private static String KEY_CREATED_AT = "created_at";
   // public static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__" ;
   // public static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__" ;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

      //  DatabaseHandler.getInstance(getApplicationContext()).resetCategories();

        TextView registerScreen = (TextView) findViewById(R.id.link_to_register);


        String loggedInUserName = PrefUtils.getFromPrefs(LoginActivity.this, getString(R.string.PREFS_LOGIN_USERNAME_KEY), "");
        String loggedInUserPassword = PrefUtils.getFromPrefs(LoginActivity.this, getString(R.string.PREFS_LOGIN_PASSWORD_KEY), "");
        String autoLoginState = PrefUtils.getFromPrefs(LoginActivity.this, getString(R.string.PREFS_AUTO_LOGIN_KEY), "false");


        DatabaseHandler.getInstance(getApplicationContext()).updateCategories();

        if (autoLoginState.equals("true")){
            inputEmail.setText(loggedInUserName);
            inputPassword.setText(loggedInUserPassword);

            //NetAsync(findViewById(R.id.btnLogin));

            Intent upanel = new Intent(getApplicationContext(), home.class);
            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            upanel.putExtra("USER_NAME", loggedInUserName);

            startActivity(upanel);
            /**
             * Close Login Screen
             **/
            finish();
        }
        else{DatabaseHandler.getInstance(getApplicationContext()).resetCategories();}
        inputEmail.setText(loggedInUserName);

        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });




        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (  ( !inputEmail.getText().toString().equals("")) && ( !inputPassword.getText().toString().equals("")) )
                {

                        PrefUtils.saveToPrefs(LoginActivity.this, getString(R.string.PREFS_LOGIN_USERNAME_KEY), inputEmail.getText().toString());
                        PrefUtils.saveToPrefs(LoginActivity.this, getString(R.string.PREFS_LOGIN_PASSWORD_KEY), inputPassword.getText().toString());
                        PrefUtils.saveToPrefs(LoginActivity.this, getString(R.string.PREFS_AUTO_LOGIN_KEY), "true");

                    NetAsync(view);
                }
                else if ( ( !inputEmail.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else if ( ( !inputPassword.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "Email field empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "Email and Password field are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void NetAsync(View view) {
        new NetCheck().execute();
    }


    private class NetCheck extends AsyncTask {
        private ProgressDialog nDialog;
        Context context = getApplicationContext();
        String th;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nDialog = new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Connecting");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects)  {
            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.000webhost.com/");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        th= "true";
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            th = "false";
            return false;
        }


        @Override
        protected void onPostExecute(Object result) {
            if (th == "true") {
                nDialog.setTitle("Logging in");

                new ProcessLogin().execute();
            } else {
                nDialog.dismiss();
                Toast.makeText(context, "Could not connect to server. Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        private class ProcessLogin extends AsyncTask {

            String email, password;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                inputEmail = (EditText) findViewById(R.id.inputEmail);
                inputPassword = (EditText) findViewById(R.id.inputPassword);
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();
            }

            @Override
            protected JSONObject doInBackground(Object[] objects) {
                APIFunctions userFunction = APIFunctions.getInstance();
                JSONObject json = userFunction.loginUser(email, password);
                return json;
            }

            @Override
            protected void onPostExecute(Object son) {
                try {
                    JSONObject json = (JSONObject) son;
                    if (json != null) {
                        if (json.getString(KEY_SUCCESS) != null) {
                            String res = json.getString(KEY_SUCCESS);
                            if (Integer.parseInt(res) == 1) {
                                nDialog.setTitle("Getting data");
                                DatabaseHandler db = DatabaseHandler.getInstance(getApplicationContext());
                                JSONObject json_user = json.getJSONObject("user");

                                /**
                                 * Clear all previous data in SQlite database.
                                 **/
                                APIFunctions logout = APIFunctions.getInstance();
                                logout.logoutUser(getApplicationContext());
                                byte[] decoded = Base64.decode(json_user.getString("profileImage").getBytes(),Base64.DEFAULT);
                                String path = saveImage(decoded);
                                db.addUser(json_user.getInt(KEY_UID), json_user.getString(KEY_FIRSTNAME), json_user.getString(KEY_LASTNAME), json_user.getString(KEY_EMAIL), path);

                                DatabaseHandler.getInstance(getApplicationContext()).getLikedCategoriesFromServer(json_user.getInt(KEY_UID));
                                /**
                                 *If JSON array details are stored in SQlite it launches the User Panel.
                                 **/
                                nDialog.dismiss();
                                Intent upanel = new Intent(getApplicationContext(), home.class);
                                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                               startActivity(upanel);
                                /**
                                 * Close Login Screen
                                 **/
                                finish();
                            } else {
                                nDialog.dismiss();
                                Toast.makeText(context, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
            }

            public String saveImage(byte[] byteArray) {
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                File dir = new File(Environment.getExternalStorageDirectory()
                        + "/Android/data/"
                        + "/Move4Klant");
                dir.mkdirs();

                File mypath = new File(dir, "ProfileImage.jpeg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.close();
                    return mypath.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();
                    return "";
                }
            }
        }
    }

}

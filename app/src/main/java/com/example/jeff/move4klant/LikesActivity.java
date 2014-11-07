package com.example.jeff.move4klant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import library.APIFunctions;
import library.DatabaseHandler;


public class LikesActivity extends Activity {
    public CheckBox[] checkBoxList;
    public String customer_ID = "1";

    private static String KEY_ID = "id";
    private static String KEY_CATEGORYNAME = "categoryName";


    private ArrayList<Category> list;

    public static String[] categoryList = {
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts",
            "Accu gereedschap",
            "Boren, bits en frezen",
            "Designproducts"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);
        NetAsync();
        getActionBar().setDisplayHomeAsUpEnabled(true);

        list = new ArrayList<Category>();

        final ListView listView = (ListView) findViewById(R.id.list_Category);

        // fill category list
        for (int i = 0; i < categoryList.length - 1; i++) {
            list.add(new Category(i, categoryList[i]));
        }

        listView.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_list_item_multiple_choice, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category c = (Category) listView.getAdapter().getItem(i);
                Toast.makeText(getApplicationContext(), c.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                String message = "";
                String checked;
                for (int i = 0; i < categoryList.length; i++) {
                    if (checkBoxList[i].isChecked()) {
                        checked = "true";
                    } else {
                        checked = "false";
                    }
                    message = message + categoryList[i] + ": " + checked + "\n";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel:
                Intent intent = new Intent(this, ManageAccount.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_likes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }




    // ------------------------NETWORK CONNECTION-------------------------------

    public void NetAsync() {
        new NetCheck().execute();
    }


    private class NetCheck extends AsyncTask {
        private ProgressDialog nDialog;
        Context context = getApplicationContext();
        String th;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            nDialog = new ProgressDialog(LikesActivity.this);
            nDialog.setTitle("Connecting");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
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
                        th = "true";
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
                nDialog.setTitle("Getting data");

                new GetCategories().execute();
            } else {
                nDialog.dismiss();
                Toast.makeText(context, "Could not connect to server. Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        private class GetCategories extends AsyncTask {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected JSONObject doInBackground(Object[] objects) {
                APIFunctions api = APIFunctions.getInstance();
                JSONObject json = api.getLikes(customer_ID);
                return json;
            }

            @Override
            protected void onPostExecute(Object son) {
                try {
                    JSONArray json = (JSONArray) son;
                    if (json != null) {
                        nDialog.setTitle("Getting data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                        Log.d("test", json.toString());
                        // Clear all previous data in SQlite database.

                        // db.addCategory(json.getString(KEY_ID), json.getString(KEY_CATEGORYNAME));

                        // If JSON array details are stored in SQlite it launches the User Panel.


                        nDialog.dismiss();

                        /**
                         * Close Login Screen
                         */
                        finish();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
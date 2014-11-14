package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import library.Category;
import library.DatabaseHandler;


public class ManageAccount extends Activity {

    private TextView tv_firstName, tv_lastName, tv_street, tv_postalCode, tv_city, tv_email;
    private DatabaseHandler db;
    private List<Category> savedLikes;
    private ImageView profileImage;


    //dummy data
    private int db_User_ID       = 1;
    private String db_FirstName  = "Leo";
    private String db_LastName   = "van der Zee";
    private String db_Street     = "Zuiderkerkstraat";
    private String db_HouseNumber= "27F";
    private String db_PostalCode = "8011 HE";
    private String db_City       = "Zwolle";
    private String db_email      = "lzee100@gmail.com";

    User user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        user = new User(getApplicationContext(), db_User_ID, db_FirstName,db_LastName,db_Street,db_HouseNumber,db_PostalCode,db_City,db_email);

        TableLayout table = (TableLayout)findViewById(R.id.table_ManageAccount_Category);
        profileImage = (ImageView)findViewById(R.id.ivImage);
        profileImage.setImageBitmap(user.getImage());

        db = new DatabaseHandler();


        tv_firstName    = (TextView)findViewById(R.id.tvName);
        tv_lastName     = (TextView)findViewById(R.id.tvLastName);
        tv_street       = (TextView)findViewById(R.id.tvAdress);
        tv_postalCode   = (TextView)findViewById(R.id.tvPostalCode);
        tv_city         = (TextView)findViewById(R.id.tvCity);
        tv_email        = (TextView)findViewById(R.id.tvEmail);

        String streetAndHouseNumber = user.getStreet() + " " + user.getHouseNumber();

        tv_firstName.setText(user.getName());
        tv_lastName.setText(user.getLastName());
        tv_street.setText(streetAndHouseNumber);
        tv_postalCode.setText(user.getPostalCode());
        tv_city.setText(user.getCity());
        tv_email.setText(user.getEmail());

        savedLikes = DatabaseHandler.getInstance(getApplicationContext()).getLikedCategories();

        TableLayout tl = new TableLayout(this);

        for (Category category : savedLikes) {
            TableRow tr = new TableRow(this);
            Log.v("Category", category.getName());
            TextView label = new TextView(this);
            label.setTextSize(16);
            label.setText(category.getName());
            tr.addView(label);

            tl.addView(tr);
        }
        table.addView(tl);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.manageAccount_Change_User_Info:
                Intent i = new Intent(getApplicationContext(), EditUserInfoActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }
}

package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import library.UpdateFromServer;


public class home extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        UpdateFromServer.getInstance(getApplicationContext()).updateOffers(getApplicationContext());
        UpdateFromServer.getInstance(getApplicationContext()).updateBeacons(getApplicationContext());
    }

    public void onClickManageProfile(View v)
    {
        Intent intent = new Intent(this, ManageAccount.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.ad_test:
                showAd(1);
                return true;
            case R.id.action_settings:
                Intent settings = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(settings);
                overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        StopscanLeDevice();
        super.onBackPressed();
        finish();
    }

    private void StopscanLeDevice() {
        //stop scan
    }

    private void showAd(int id) {
        Intent i = new Intent(getApplicationContext(), OfferActivity.class);
        i.putExtra("offerID", id);
        startActivity(i);
    }
}

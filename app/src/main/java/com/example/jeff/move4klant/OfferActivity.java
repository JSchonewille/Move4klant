package com.example.jeff.move4klant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import library.Offer;
import library.PrefUtils;

/**
 * Created by Sander on 7-11-2014.
 */
public class OfferActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offer);
        Bundle bundle = getIntent().getExtras();

        int offerID = bundle.getInt("offerID");
        Offer offer = PrefUtils.getOfferFromPrefs(getApplicationContext(),getString(R.string.PREFS_OFFERS), offerID);

        TextView t = (TextView) findViewById(R.id.offerMessage);
        t.setText(offer.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ad, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_cancel:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

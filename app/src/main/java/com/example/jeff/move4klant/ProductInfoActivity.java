package com.example.jeff.move4klant;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import library.DatabaseHandler;
import Objects.Product;

/**
 * Created by Sander on 7-11-2014.
 */
public class ProductInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_info);
        Bundle bundle = getIntent().getExtras();

        int productID = bundle.getInt("productID");
        Product product = DatabaseHandler.getInstance(getApplicationContext()).getProductByID(productID);

        if (product!=null) {
            TextView t = (TextView) findViewById(R.id.Product_Title);
            t.setText(product.getName());

            TextView t2 = (TextView) findViewById(R.id.product_description);
            t2.setText(product.getDescription());
        }
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

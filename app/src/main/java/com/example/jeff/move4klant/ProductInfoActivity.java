package com.example.jeff.move4klant;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import library.DatabaseFunctions;
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
        DatabaseFunctions db = new DatabaseFunctions(getApplicationContext());
        int productID = bundle.getInt("productID");
        Product product = DatabaseHandler.getInstance(getApplicationContext()).getProductByID(productID);

        if (product!=null) {
            TextView t = (TextView) findViewById(R.id.Product_Title);
            ImageView i_productImage = (ImageView) findViewById(R.id.i_productImage);
            t.setText(product.getName());

            String base = db.getimages(product.getImage());
            byte[] imgbyte = Base64.decode(base, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(imgbyte, 0, imgbyte.length);
            if (bmp != null)
            {
                i_productImage.setBackground(null);
               i_productImage.setImageBitmap(bmp);
            }

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

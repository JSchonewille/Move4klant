package com.example.jeff.move4klant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import Objects.Category;
import Objects.User;
import library.DatabaseHandler;


public class ManageAccount extends Activity {

    private TextView tv_firstName, tv_lastName, tv_street, tv_postalCode, tv_city, tv_email;
    private List<Category> savedLikes;
    private ImageView profileImage;

    User user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        profileImage = (ImageView) findViewById(R.id.ivImage);

        User userDetails = DatabaseHandler.getInstance(getApplicationContext()).getUser();
        user = userDetails;

        TableLayout table = (TableLayout)findViewById(R.id.table_ManageAccount_Category);
        if (user.getFilePath().equals("")){
            Bitmap defaultImage = BitmapFactory.decodeResource(getResources(), R.drawable.emptyprofile);
            Bitmap roundedDefaultImage = this.roundCornerImage(defaultImage, 15);
            profileImage.setImageBitmap(roundedDefaultImage);
        }
        else {
            File imgFile = new  File(user.getFilePath());

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Bitmap roundedB = this.roundCornerImage(myBitmap, 15);
                user.setImage(roundedB);
                profileImage.setImageBitmap(user.getImage());
            }
        }



        tv_firstName    = (TextView)findViewById(R.id.tvName);
        tv_lastName     = (TextView)findViewById(R.id.tvLastName);
       // tv_street       = (TextView)findViewById(R.id.tvAdress);
       // tv_postalCode   = (TextView)findViewById(R.id.tvPostalCode);
       // tv_city         = (TextView)findViewById(R.id.tvCity);
        tv_email        = (TextView)findViewById(R.id.tvEmail);

        String streetAndHouseNumber = user.getStreet() + " " + user.getHouseNumber();

        tv_firstName.setText(user.getName());
        tv_lastName.setText(user.getLastName());
       // tv_street.setText(streetAndHouseNumber);
       // tv_postalCode.setText(user.getPostalCode());
       // tv_city.setText(user.getCity());
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
                overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
                finish();
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

    public Bitmap roundCornerImage(Bitmap src, float round) {
        // Source image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create result bitmap output
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // set canvas for painting
        Canvas canvas = new Canvas(result);
        canvas.drawARGB(0, 0, 0, 0);

        // configure paint
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        // configure rectangle for embedding
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);

        // draw Round rectangle to canvas
        canvas.drawRoundRect(rectF, round, round, paint);

        // create Xfer mode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // draw source image to canvas
        canvas.drawBitmap(src, rect, rect, paint);

        // return final image
        return result;
    }
}

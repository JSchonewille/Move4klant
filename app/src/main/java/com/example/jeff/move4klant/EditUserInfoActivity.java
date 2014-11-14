package com.example.jeff.move4klant;

// manifest user licence
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import library.Category;
import library.DatabaseHandler;


public class EditUserInfoActivity extends Activity {

    private EditText etName, etLastName, etStreet, etPostalCode, etHouseNumber, etCity, etEmail;
    private Bitmap bitmap;
    private ImageView imageView;
    private String name, lastName, street, postalCode, houseNumber, city, email;
    private byte[] byteArray;
    private List<Category> savedLikes;
    private Button changeCategory;


    //dummy data
    private int db_User_ID       = 1;
    private String db_FirstName  = "Leo";
    private String db_LastName   = "van der Zee";
    private String db_Street     = "Zuiderkerkstraat";
    private String db_HouseNumber= "27F";
    private String db_PostalCode = "8011 HE";
    private String db_City       = "Zwolle";
    private String db_email      = "lzee100@gmail.com";

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TableLayout table = (TableLayout)findViewById(R.id.tableViewCategory_EditUserInfo);
        user = new User(getApplication(), db_User_ID, db_FirstName,db_LastName,db_Street,db_HouseNumber,db_PostalCode,db_City,db_email);

        etName          = (EditText)findViewById(R.id.etName);
        etLastName      = (EditText)findViewById(R.id.etLastName);
        etStreet        = (EditText)findViewById(R.id.etStreet);
        etHouseNumber   = (EditText)findViewById(R.id.etHouseNumber);
        etPostalCode    = (EditText)findViewById(R.id.etPostalCode);
        etCity          = (EditText)findViewById(R.id.etCity);
        etEmail         = (EditText)findViewById(R.id.etEmail);

        // set db info in Edit text
        etName.setHint(user.getName());
        etLastName.setHint(user.getLastName());
        etStreet.setHint(user.getStreet());
        etHouseNumber.setHint(user.getHouseNumber());
        etPostalCode.setHint(user.getPostalCode());
        etCity.setHint(user.getCity());
        etEmail.setHint(user.getEmail());

        imageView = (ImageView)findViewById(R.id.ivProfileImageEdit);
        imageView.setImageBitmap(user.getImage());
        changeCategory = (Button)findViewById(R.id.btChangeCategory);

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
        getMenuInflater().inflate(R.menu.menu_edit_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.saveUserInfo:

                name            = etName.getText().toString();
                lastName        = etLastName.getText().toString();
                street          = etStreet.getText().toString();
                houseNumber     = etHouseNumber.getText().toString();
                postalCode      = etPostalCode.getText().toString();
                city            = etCity.getText().toString();
                email           = etEmail.getText().toString();

                // if input is empty, get last db info
                if (name.matches("")){
                    name = etName.getHint().toString();
                }
                if (lastName.matches("")){
                    lastName = etLastName.getHint().toString();
                }
                if (street.matches("")){
                    street = etStreet.getHint().toString();
                }
                if (houseNumber.matches("")){
                    houseNumber = etHouseNumber.getHint().toString();
                }
                if (postalCode.matches("")){
                    postalCode = etPostalCode.getHint().toString();
                }
                if (city.matches("")){
                    city = etCity.getHint().toString();
                }
                if (email.matches("")){
                    email = etEmail.getHint().toString();
                }

                // reset values of the user
                user = new User(getApplicationContext(), db_User_ID, name, lastName, street, houseNumber, postalCode, city, email);
                //user.setImage(bitmap);
                // send user to db and update server
                DatabaseHandler.getInstance(getApplicationContext()).uploadUserImage(user.getUserID(), byteArray);


                Intent i = new Intent(getApplicationContext(), ManageAccount.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfileImageEdit:
                Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                break;
            case R.id.btChangeCategory:
                Intent intent2 = new Intent(getApplicationContext(), LikesActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
        // Inflate the menu; this adds items to the action bar if it is present.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            Uri targetUri = data.getData();
            // save image to sd card
            boolean success = false;
            File sdCardDirectory = Environment.getExternalStorageDirectory();
            File image = new File(sdCardDirectory, "ProfileImage.png");
            FileOutputStream outStream;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageView.setImageBitmap(bitmap);
                //saveImageToSD(bitmap);




                //user.setImage(bitmap);

//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byteArray = stream.toByteArray();
//
//                // save image to sd-card
//                outStream = new FileOutputStream(image);
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//        /* 100 to keep full quality of the image */
//
//                outStream.flush();
//                outStream.close();
//                success = true;
//
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (success) {
//                Toast.makeText(getApplicationContext(), "Image saved with success",
//                        Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Error during image saving", Toast.LENGTH_LONG).show();
//            }
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
    }

//    public String[] saveImageToSD(Bitmap b){
//        String[] response;
//
//            response[0] = "0";
//            Bitmap bitmap;
//            OutputStream output;
//
//            // Retrieve the image from the res folder
//            bitmap = b;
//
//            // Find the SD Card path
//            File filepath = Environment.getExternalStorageDirectory();
//
//            // Create a new folder in SD Card
//            File dir = new File(filepath.getAbsolutePath()
//                    + "/Save Image Tutorial/");
//            dir.mkdirs();
//
//            // Create a name for the saved image
//            File file = new File(dir, "myimage.png");
//
//            // Show a toast message on successful save
//            Toast.makeText(MainActivity.this, "Image Saved to SD Card",
//                    Toast.LENGTH_SHORT).show();
//            try {
//
//                output = new FileOutputStream(file);
//
//                // Compress into png format image from 0% - 100%
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
//                output.flush();
//                output.close();
//                response[0] = "1";
//            }
//
//            catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        return response;
//    }
}


package com.example.jeff.move4klant;

// manifest user licence
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import library.Category;
import library.DatabaseHandler;
import library.User;


public class EditUserInfoActivity extends Activity {

    private EditText etName, etLastName, etStreet, etPostalCode, etHouseNumber, etCity, etEmail;
    private Bitmap bitmap;
    private ImageView imageView;
    private User user;
    private int userID;
    private String name, lastName, street, postalCode, houseNumber, city, email;
    private byte[] byteArray;
    private List<Category> savedLikes;
    private Button changeCategory;
    private Boolean response = false;
    private String filePath;

    private ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        imageView = (ImageView)findViewById(R.id.ivProfileImageEdit);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TableLayout table = (TableLayout)findViewById(R.id.tableViewCategory_EditUserInfo);
        user = DatabaseHandler.getInstance(getApplicationContext()).getUser();
        userID = user.getUserID();

        etName          = (EditText)findViewById(R.id.etName);
        etLastName      = (EditText)findViewById(R.id.etLastName);
        //etStreet        = (EditText)findViewById(R.id.etStreet);
       // etHouseNumber   = (EditText)findViewById(R.id.etHouseNumber);
       // etPostalCode    = (EditText)findViewById(R.id.etPostalCode);
      //  etCity          = (EditText)findViewById(R.id.etCity);
        etEmail         = (EditText)findViewById(R.id.etEmail);

        // set db info in Edit text
        etName.setHint(user.getName());
        etLastName.setHint(user.getLastName());
       // etStreet.setHint(user.getStreet());
       // etHouseNumber.setHint(user.getHouseNumber());
      //  etPostalCode.setHint(user.getPostalCode());
      //  etCity.setHint(user.getCity());
        etEmail.setHint(user.getEmail());

        if (user.getFilePath().equals("")){
            imageView.setImageResource(R.drawable.emptyprofile);
        }
        else {
            File imgFile = new  File(user.getFilePath());

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                user.setImage(myBitmap);
                imageView.setImageBitmap(user.getImage());
            }
        }

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
                Log.v("filePath: ", user.getFilePath());

                // reset values of the user
                user = new User(getApplicationContext(),userID, name, lastName, street, houseNumber, postalCode, city, email);
                user.setFilePath(filePath);
                DatabaseHandler.getInstance(getApplicationContext()).addUser(user.getName(), user.getLastName(), user.getStreet(), user.getPostalCode(), user.getHouseNumber(), user.getCity(), user.getEmail(), user.getFilePath());
                user.setImage(bitmap);
                //TODO send user to db and update server
                DatabaseHandler.getInstance(getApplicationContext()).uploadUserImage(user.getUserID(), byteArray);

                Intent i = new Intent(getApplicationContext(), ManageAccount.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfileImageEdit:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        .setType("image/*")
                        .putExtra("crop", "true")
                        .putExtra("aspectX", 1)
                        .putExtra("aspectY", 1)
                        .putExtra("outputX", 200)
                        .putExtra("outputY", 200)
                        .putExtra("scale", "true")
                        .putExtra(MediaStore.EXTRA_OUTPUT, Environment.getExternalStorageDirectory()
                                + "/Android/data/"
                                + "/Move4Klant")
                        .putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
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
            File image = new File(sdCardDirectory, "ProfileImage.jpeg");
            FileOutputStream outStream;
            try {
                // get image from selection
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                imageView.setImageBitmap(bitmap);
                //saveImageToSD(bitmap);

                // save to sd
                saveImageToSD(bitmap);

                // if correctly saved, show message
                if (response) {
                    // Show a toast message on successful save
                    Toast.makeText(getApplicationContext(), "Image Saved to SD Card",
                            Toast.LENGTH_SHORT).show();
                }

                // set image to this view
                user.setImage(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                //user.setImage(bitmap);


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

    public boolean saveImageToSD(Bitmap b){

            Bitmap bitmap;
            OutputStream output;

            // Retrieve the image from the res folder
            bitmap = b;

            // Create a new folder in SD Card
            File dir = new File(Environment.getExternalStorageDirectory()
                    + "/Android/data/"
                    + "/Move4Klant");
            dir.mkdirs();

            // Create a name for the saved image
            File file = new File(dir, "ProfileImage.jpeg");
            Log.v("FilePath:  ", file.toString());
            Log.v("ABSOLUUT filePath: ", file.getAbsolutePath().toString());
            Log.v("ABSOLUUT filePath2: ", file.getPath());
            filePath = file.getAbsolutePath().toString();

            try {

                output = new FileOutputStream(file);

                // Compress into png format image from 0% - 100%
              //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
                output.flush();
                output.close();
                response = true;
            }

            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return response;
    }

}


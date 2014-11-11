package library;

/**
 * Created by Sander on 8-10-2014.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ibeacon";
    // Table names
    private static final String TABLE_LOGIN = "login";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_BEACONS = "beacons";
    private static final String TABLE_OFFERS = "offers";
    private static final String TABLE_PRODUCTS = "products";

    // Login Table Column names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "fname";
    private static final String KEY_LASTNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "uname";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    //Category Table Column names
    private static final String KEY_ID_Category = "id_Category";
    private static final String KEY_CATEGORYNAME = "categoryName";
    //Offers Table Column names
    private static final String KEY_ID_OFFER = "id_offer";
    private static final String KEY_OFFERCATEGORY = "offer_category";
    private static final String KEY_OFFERDESCRIPTION = "offer_description";
    private static final String KEY_OFFERIMAGE = "offer_image";
    //Beacon Table Column names
    private static final String KEY_BEACON_ID = "id_beacon";
    private static final String KEY_BEACON_PRODUCTID = "beaccon_product";
    private static final String KEY_BEACON_OFFERID = "beacon_offer";
    private static final String KEY_BEACON_MAJOR = "beacon_major";
    private static final String KEY_BEACON_MINOR = "beacon_minor";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
       createTables(db);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing category in database
     * */
    public void addCategory(String id, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_Category, id); // FirstName
        values.put(KEY_CATEGORYNAME, categoryName); // LastName
        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting Categories from database
     * */
    public ArrayList<Category> getSavedCategories(){
        ArrayList<Category> list = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(1));
                String category = cursor.getString(2);
                Category c = new Category(id, category);
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String fname, String lname, String email, String uname, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, fname); // FirstName
        values.put(KEY_LASTNAME, lname); // LastName
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_USERNAME, uname); // UserName
        values.put(KEY_UID, uid); // Email
        values.put(KEY_CREATED_AT, created_at); // Created At
        // Inserting Row
        db.insert(TABLE_LOGIN, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting user data from database
     * */
    public HashMap getUserDetails(){
        HashMap user = new HashMap();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            user.put("fname", cursor.getString(1));
            user.put("lname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("uname", cursor.getString(4));
            user.put("uid", cursor.getString(5));
            user.put("created_at", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        return user;
    }
    /**
     * Getting user login status
     * return true if rows are there in table
     * */
    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }



    /**
     * Storing offer in database
     * */
    public void addOffer(int id, String categoryID, String description, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID_OFFER, id); // OfferID
        values.put(KEY_OFFERCATEGORY, categoryID); // Category ID
        values.put(KEY_OFFERDESCRIPTION, description); // Offer Description
        values.put(KEY_OFFERIMAGE, image); // Offer Image
        // Inserting Row
        db.insert(TABLE_OFFERS, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Getting offers from database
     * */
    public ArrayList<Category> getSavedOffers(){
        ArrayList<Category> list = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_OFFERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(1));
                String category = cursor.getString(2);
                Category c = new Category(id, category);
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }











    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    /**
     * Re create database
     * Delete all tables and create them again
     * */
    public void resetCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CATEGORY, null, null);
        db.close();
    }

    public void createTables(SQLiteDatabase db){
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_USERNAME + " TEXT,"
                + KEY_UID + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_ID_Category + " TEXT,"
                + KEY_CATEGORYNAME + " TEXT" + ")";
        db.execSQL(CREATE_CATEGORY_TABLE);
        String CREATE_OFFERS_TABLE = "CREATE TABLE " + TABLE_OFFERS + "("
                + KEY_ID_OFFER + " INTEGER PRIMARY KEY,"
                + KEY_OFFERCATEGORY + " TEXT,"
                + KEY_OFFERDESCRIPTION + " TEXT,"
                + KEY_OFFERIMAGE + " TEXT" + ")";
        db.execSQL(CREATE_OFFERS_TABLE);
        String CREATE_BEACONS_TABLE = "CREATE TABLE " + TABLE_BEACONS + "("
                + KEY_BEACON_ID + " INTEGER PRIMARY KEY,"
                + KEY_BEACON_PRODUCTID + " INTEGER,"
                + KEY_BEACON_OFFERID + " INTEGER,"
                + KEY_BEACON_MAJOR + " INTEGER,"
                + KEY_BEACON_MINOR + " INTEGER" + ")";
        db.execSQL(CREATE_BEACONS_TABLE);
    }


}
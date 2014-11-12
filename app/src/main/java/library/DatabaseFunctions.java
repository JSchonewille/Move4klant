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
import java.util.List;

public class DatabaseFunctions extends SQLiteOpenHelper {


    //region Table and column names
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
    private static final String KEY_ID_Category = "id";
    private static final String KEY_CATEGORYNAME = "categoryName";
    private static final String KEY_CATEGORYLIKED = "liked";
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
    //endregion



    public DatabaseFunctions(Context context) {
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
     * Category functions
     * */
    public void addCategory(int id, String categoryName, int liked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); //
        values.put(KEY_CATEGORYNAME, categoryName); //
        values.put(KEY_CATEGORYLIKED, liked); //
        // Inserting Row
        db.insert(TABLE_CATEGORY, null, values);
        db.close(); // Closing database connection
    }
    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> list = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String category = cursor.getString(1);
                int liked = cursor.getInt(2);
                Category c = new Category(id, category, liked);
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }
    public ArrayList<Category> getLikedCategories(){
        ArrayList<Category> list = new ArrayList<Category>();
        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY + " WHERE " + KEY_CATEGORYLIKED + " =  1" ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String category = cursor.getString(1);
                int liked = cursor.getInt(2);
                Category c = new Category(id, category, liked);
                list.add(c);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }
    public void saveLikedCategories (List<Category> list){


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
     * Offer functions
     * */
    public void addOffer(int id, int categoryID, String description, String image) {
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
    public ArrayList<Offer> getSavedOffers(){
        ArrayList<Offer> list = new ArrayList<Offer>();
        String selectQuery = "SELECT  * FROM " + TABLE_OFFERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Offer o;
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = cursor.getInt(0);
                int categoryID= cursor.getInt(1);
                String description = cursor.getString(2);
                String image = cursor.getString(3);
                o = new Offer(id, categoryID,description,image);
                list.add(o);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }
    public Offer getOfferByID(int offerID){
        Offer o = null;
        String selectQuery = "SELECT  * FROM " + TABLE_OFFERS + " WHERE " + KEY_ID_OFFER + " = " + offerID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            int id = cursor.getInt(0);
            int categoryID= cursor.getInt(1);
            String description = cursor.getString(2);
            String image = cursor.getString(3);
            o = new Offer(id, categoryID,description,image);
        }
        cursor.close();
        db.close();
        // return category list
        return o;
    }


    /**
     * Beacon functions
     * */
    public void addBeacon(int beaconID, int productID, int offerID, int minor, int major) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BEACON_ID, beaconID); // BEACON ID
        values.put(KEY_BEACON_MAJOR, major); // MAJOR
        values.put(KEY_BEACON_MINOR, minor); // MINOR
        values.put(KEY_BEACON_OFFERID, offerID); // OFFERID
        values.put(KEY_BEACON_PRODUCTID, productID); // PRODUCTID

        // Inserting Row
        db.insert(TABLE_BEACONS, null, values);
        db.close(); // Closing database connection
    }
    public ArrayList<ibeacon> getAllBeacons(){
        ArrayList<ibeacon> list = new ArrayList<ibeacon>();
        String selectQuery = "SELECT  * FROM " + TABLE_BEACONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            do {
                int id = cursor.getInt(1);
                int productID= cursor.getInt(2);
                int offerID= cursor.getInt(3);
                int major= cursor.getInt(4);
                int minor= cursor.getInt(5);
                ibeacon b = new ibeacon(id, offerID,productID,major,minor);
                list.add(b);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return category list
        return list;
    }
    public ibeacon getBeaconByMinor(int minorID){
        ibeacon b = null;
        String selectQuery = "SELECT  * FROM " + TABLE_BEACONS + " WHERE " + KEY_BEACON_MINOR + " = " + minorID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){

                int id = cursor.getInt(1);
                int productID= cursor.getInt(2);
                int offerID= cursor.getInt(3);
                int major= cursor.getInt(4);
                int minor= cursor.getInt(5);
                b = new ibeacon(id, offerID,productID,major,minor);


        }
        cursor.close();
        db.close();
        // return category list
        return b;
    }

    /**
     * Empty tables
     * */
    public void resetTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();
    }
    public void resetBeaconTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_BEACONS, null, null);
        db.close();
    }
    public void resetOfferTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_OFFERS, null, null);
        db.close();
    }
    public void resetCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CATEGORY, null, null);
        db.close();
    }

    /**
     * Create tables
     * */
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
                + KEY_CATEGORYNAME + " TEXT,"
                + KEY_CATEGORYLIKED + " INTEGER"+ ")";
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
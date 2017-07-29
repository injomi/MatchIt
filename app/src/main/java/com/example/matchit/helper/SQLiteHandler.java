package com.example.matchit.helper;

/**
 * Created by Joel on 04-Jun-17.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "sql12179354";

    // Login table name
    private static final String TABLE_USER = "acc_volunteer";

    private static final String ORG_USER = "org_info";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_NRIC = "nric";
    private static final String KEY_ORG_NAME = "org_name";
    private static final String KEY_UEN = "uen";
    private static final String KEY_CONTACT = "liason_contact";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_POSTALCODE = "postalcode";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_CONTACTNUMBER = "contactnumber";
    private static final String KEY_NATIONALITY = "nationality";
    private static final String KEY_RACE = "race";
    private static final String KEY_SPECIALIZATION = "specialization";
    private static final String KEY_OCCUPATION = "occupation";
    private static final String KEY_LANGSPOKEN = "languagespoken";
    private static final String KEY_LANGWRITTEN = "languagewritten";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," +
                KEY_CREATED_AT + " TEXT," + KEY_NRIC + " TEXT," +
                KEY_ORG_NAME + " TEXT," +  KEY_UEN + " TEXT," +
                KEY_CONTACT + " TEXT," + KEY_ADDRESS + " TEXT," +
                KEY_POSTALCODE + " TEXT," + KEY_GENDER + " TEXT," +
                KEY_CONTACTNUMBER + " TEXT," + KEY_NATIONALITY + " TEXT," +
                KEY_RACE + " TEXT," + KEY_SPECIALIZATION + " TEXT,"  +
                KEY_OCCUPATION + " TEXT,"  + KEY_LANGSPOKEN + " TEXT,"  +
                KEY_LANGWRITTEN + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(JSONObject jObj) {
        String uid,name,email,created_at,nric,org_name,uen,liason_contact,address,postalcode,gender,contactnumber,nationality,
                race,specialization,occupation,languagespoken,languagewritten;
        try {
             uid = jObj.getString("uid");
             JSONObject user = jObj.getJSONObject("user");
             name = user.getString("name");
             email = user.getString("email");
             created_at = user.getString("created_at");
             nric = user.getString("nric");
             org_name = user.getString("org_name");
             uen = user.getString("uen");
             liason_contact = user.getString("liason_contact");
             address = user.getString("address");
             postalcode = user.getString("postalcode");
             gender = user.getString("gender");
             contactnumber = user.getString("contactnumber");
             nationality = user.getString("nationality");
             race = user.getString("race");
             specialization = user.getString("specialization");
             occupation = user.getString("occupation");
             languagespoken = user.getString("languagespoken");
             languagewritten = user.getString("languagewritten");
        }catch(JSONException e){
            Log.i("Test",e.toString());
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Test", "Add name: " + name + ", Add email: " + email);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at); // Created At
        values.put(KEY_NRIC, nric); //NRIC
        values.put(KEY_ORG_NAME, org_name); //Org name
        values.put(KEY_UEN, uen); //UEN
        values.put(KEY_CONTACT, liason_contact); //Contact Number
        values.put(KEY_ADDRESS, address);
        values.put(KEY_POSTALCODE, postalcode); // Created At
        values.put(KEY_GENDER, gender); //NRIC
        values.put(KEY_CONTACTNUMBER, contactnumber); //Org name
        values.put(KEY_NATIONALITY, nationality); //UEN
        values.put(KEY_RACE, race); //Contact Number
        values.put(KEY_SPECIALIZATION, specialization); //Contact Number
        values.put(KEY_OCCUPATION, occupation);
        values.put(KEY_LANGSPOKEN, languagespoken); // Created At
        values.put(KEY_LANGWRITTEN, languagewritten); //NRIC

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updateOrg(JSONObject jObj) {
        String uid,name,org,contact,uen;
        try {
            uid = jObj.getString("unique_id");
            name = jObj.getString("name");
            org = jObj.getString("org_name");
            contact = jObj.getString("liason_contact");
            uen = jObj.getString("uen");

        }catch(JSONException e){
            Log.i("Test",e.toString());
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
//        Log.i("Test", "Add name: " + name + ", Add email: " + email);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_ORG_NAME,org);
        values.put(KEY_CONTACT,contact);
        values.put(KEY_UEN,uen);
        // Inserting Row
        long id = db.update(TABLE_USER, values, KEY_UID +"='"+uid + "'", null);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    public void updateUser(JSONObject jObj) {
        String uid,name,email,address,postalcode,contactnumber,languagespoken,languagewritten;
        try {
            uid = jObj.getString("unique_id");
            name = jObj.getString("name");
            email = jObj.getString("email");
            address = jObj.getString("address");
            postalcode = jObj.getString("postalcode");
            contactnumber = jObj.getString("contactnumber");
            languagespoken = jObj.getString("languagespoken");
            languagewritten = jObj.getString("languagewritten");
        }catch(JSONException e){
            Log.i("Test",e.toString());
            return;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i("Test", "Add name: " + name + ", Add email: " + email);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_POSTALCODE, postalcode); // Created At
        values.put(KEY_CONTACTNUMBER, contactnumber); //Org name
        values.put(KEY_LANGSPOKEN, languagespoken); // Created At
        values.put(KEY_LANGWRITTEN, languagewritten); //NRIC

        // Inserting Row
        long id = db.update(TABLE_USER, values, KEY_UID +"='"+uid + "'", null);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
            user.put("nric", cursor.getString(5));
            user.put("org_name", cursor.getString(6));
            user.put("uen", cursor.getString(7));
            user.put("liason_contact", cursor.getString(8));
            user.put("address", cursor.getString(9));
            user.put("postalcode", cursor.getString(10));
            user.put("gender", cursor.getString(11));
            user.put("contactnumber", cursor.getString(12));
            user.put("nationality", cursor.getString(13));
            user.put("race", cursor.getString(14));
            user.put("specialization", cursor.getString(15));
            user.put("occupation", cursor.getString(16));
            user.put("languagespoken", cursor.getString(17));
            user.put("languagewritten", cursor.getString(18));
        }
        cursor.close();
        db.close();
        // return user
        Log.d("Test", "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public HashMap<String, String> getOrgDetails() {
        HashMap<String, String> org = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + ORG_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            org.put("org_name", cursor.getString(1));
            org.put("org_address", cursor.getString(2));
            org.put("org_contact", cursor.getString(3));
            org.put("org_who", cursor.getString(4));
            org.put("org_how_they", cursor.getString(5));
            org.put("org_how_you", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d("Test", "Fetching user from Sqlite: " + org.toString());

        return org;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
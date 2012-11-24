package com.connectutb.callshield.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	 
/* Our database variables */
private static final String DATABASE_NAME = "blockDb";
private static final int DATABASE_VERSION = 1;
/* Our tables and fields */
private static final String TABLE_BLOCKLIST = "blocklist";
private static final String TABLE_BLOCKLOG = "blocklog";
private static final String BLOCKLIST_ID = "id";
private static final String BLOCKLIST_NUMBER = "number";
private static final String BLOCKLIST_NAME = "name";
private static final String LOG_TIMESTAMP ="timestamp";
 
//constructor
public DbHelper(Context context){
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
}
/* The onCreate() function will create our database and table. First we define a string containing our SQL query, and then we execute it. */
@Override
public void onCreate(SQLiteDatabase db){
	//Create block table
    String CREATE_BLOCK_TABLE = "CREATE TABLE " + TABLE_BLOCKLIST + "("
            + BLOCKLIST_ID + " INTEGER PRIMARY KEY," + BLOCKLIST_NUMBER + " TEXT,"
            + BLOCKLIST_NAME + " TEXT" + ")";
    //Execute the query
    db.execSQL(CREATE_BLOCK_TABLE);
    //Create log table
    String CREATE_LOG_TABLE = "CREATE TABLE " + TABLE_BLOCKLOG + "("
            + BLOCKLIST_ID + " INTEGER PRIMARY KEY," + BLOCKLIST_NUMBER + " TEXT,"
            + BLOCKLIST_NAME + " TEXT, " + LOG_TIMESTAMP + " TEXT)";
    db.execSQL(CREATE_LOG_TABLE);
}
 
/* The onUpgrade() function will handle upgrade of our database. In this case we will just drop the older table if it exists and create a new one. */
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKLIST);
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKLOG);
    //Then we run the onCreate() method again //
    onCreate(db);
}

void addBlockedNumber(String number, String name){
	//Add a new blocked number
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(BLOCKLIST_NUMBER, number);
	    values.put(BLOCKLIST_NAME, name);
	 
	    /*Inserting the entry */
	    db.insert(TABLE_BLOCKLIST, null, values);
	    db.close(); //close the database connection
	}

void addBlockedLogItem(String number, String name){
	//Add a new blocked number log item
	    SQLiteDatabase db = this.getWritableDatabase();
	 //Grab current time
	    Date date = new Date();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
	    String formattedDate = sdf.format(date);
	    
	    ContentValues values = new ContentValues();
	    values.put(BLOCKLIST_NUMBER, number);
	    values.put(BLOCKLIST_NAME, name);
	    values.put(LOG_TIMESTAMP, formattedDate);
	 
	    /*Inserting the entry */
	    db.insert(TABLE_BLOCKLOG, null, values);
	    db.close(); //close the database connection
	}
}
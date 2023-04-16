package com.jordanheim.jerseytrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class to perform the CRUD operations on the Jersey Database that will be used
 * within the app
 */
public class JerseyDatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "JerseysData.DB";
    private static final String TABLE_NAME = "JerseysTable";

    private static final String COLUMN_0_ID = "id";
    private static final String COLUMN_1_USER_USERNAME = "username";
    private static final String COLUMN_2_JERSEY_NAME = "jerseyname";
    private static final String COLUMN_3_JERSEY_TYPE = "jerseytype";
    private static final String COLUMN_4_JERSEY_QUANTITY = "jerseyquantity";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            COLUMN_0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_1_USER_USERNAME + " VARCHAR, " +
            COLUMN_2_JERSEY_NAME + " VARCHAR, " +
            COLUMN_3_JERSEY_TYPE + " VARCHAR, " +
            COLUMN_4_JERSEY_QUANTITY + " VARCHAR" + ");";

    public JerseyDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    /**
     * Create a jersey and insert into the Jersey database
     * @param jersey - jersey object created by the user
     */
    public void createJersey(Jersey jersey)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_1_USER_USERNAME, jersey.getUsername());
        values.put(COLUMN_2_JERSEY_NAME, jersey.getJerseyName());
        values.put(COLUMN_3_JERSEY_TYPE, jersey.getJerseyType());
        values.put(COLUMN_4_JERSEY_QUANTITY, jersey.getJerseyQty());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    /**
     * Read a Jersey object from the database
     * NOTE: Method currently not in use
     * @param id - id is used to get the correct jersey information
     */
    public Jersey readJersey(int id)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.query(TABLE_NAME,
                new String[] {COLUMN_0_ID, COLUMN_1_USER_USERNAME, COLUMN_2_JERSEY_NAME, COLUMN_3_JERSEY_TYPE, COLUMN_4_JERSEY_QUANTITY},
                COLUMN_0_ID + " = ?", new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Jersey jersey = new Jersey(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        cursor.close();
        return jersey;
    }

    /**
     * Update a Jersey that is contained within the Jersey database
     * @param jersey - jersey object that is within the database
     * @return the number of rows affected by the update
     */
    public int updateJersey(Jersey jersey)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_1_USER_USERNAME, jersey.getUsername());
        values.put(COLUMN_2_JERSEY_NAME, jersey.getJerseyName());
        values.put(COLUMN_3_JERSEY_TYPE, jersey.getJerseyType());
        values.put(COLUMN_4_JERSEY_QUANTITY, jersey.getJerseyQty());

        return database.update(TABLE_NAME, values, COLUMN_0_ID + " = ?",
                new String[] {String.valueOf(jersey.getId())} );
    }

    /**
     * Delete a Jersey that is within the Jersey database
     * @param jersey - jersey object that is selected by the user
     */
    public void deleteJersey(Jersey jersey)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME, COLUMN_0_ID + " = ?",
                new String[] {String.valueOf(jersey.getId())} );
        database.close();
    }

    /**
     * Get all of the Jerseys in the jersey Database
     * @return list of Jerseys that are within the Jersey Database
     */
    public List<Jersey> getAllJerseys()
    {
        List<Jersey> jerseyList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            do
            {
                Jersey jersey = new Jersey();
                jersey.setId((Integer.parseInt(cursor.getString(0))));
                jersey.setUsername(cursor.getString(1));
                jersey.setJerseyName(cursor.getString(2));
                jersey.setJerseyType(cursor.getString(3));
                jersey.setJerseyQty(cursor.getString(4));

                jerseyList.add(jersey);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jerseyList;
    }

    /**
     * Get the number of Jerseys currently in the database
     * @return the number of Jerseys in the Jersey database
     */
    public int getJerseysCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(countQuery, null);
        int jerseysTotal = cursor.getCount();
        cursor.close();
        return jerseysTotal;
    }

}

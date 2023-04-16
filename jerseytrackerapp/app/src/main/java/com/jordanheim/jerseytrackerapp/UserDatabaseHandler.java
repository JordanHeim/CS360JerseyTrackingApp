package com.jordanheim.jerseytrackerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * class to perform the Create method of the CRUD operations on the User Database that will be used
 * within the app
 */
public class UserDatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "UsersData.DB";

    public static final String TABLE_NAME = "UsersTable";

    public static final String COLUMN_0_ID = "id";
    public static final String COLUMN_1_USERNAME = "username";
    public static final String COLUMN_2_PASSWORD = "password";

    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + " (" +
            COLUMN_0_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            COLUMN_1_USERNAME + " VARCHAR," +
            COLUMN_2_PASSWORD + " VARCHAR" + ");";

    public UserDatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        database.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    /**
     * Create a User and insert into the User database
     * @param user - user object created by the user
     */
    public void createUser(User user)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_1_USERNAME, user.getUsername());
        values.put(COLUMN_2_PASSWORD, user.getPassword());

        database.insert(TABLE_NAME, null, values);
        database.close();
    }
}

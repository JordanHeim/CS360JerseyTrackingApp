package com.jordanheim.jerseytrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class to display the register screen and add functionality
 * to the various components within
 */
public class RegisterActivity extends AppCompatActivity
{
    Button RegisterBtn;
    EditText UsernameHolder, PasswordHolder;
    Boolean EmptyHolder;
    SQLiteDatabase database;
    UserDatabaseHandler handler;
    String F_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize editText, button, and database handler
        UsernameHolder = findViewById(R.id.regUsernameEditText);
        PasswordHolder = findViewById(R.id.regPasswordEditText);
        RegisterBtn = findViewById(R.id.regSignUpBtn);
        handler = new UserDatabaseHandler(this);

        // Add click listener to RegisterBtn
        RegisterBtn.setOnClickListener(view -> {
            String message = checkEditTextNotEmpty();

            if(!EmptyHolder)
            {
                // Determine if username already exits within the User database
                checkUsernameAlreadyExists();

                // Empty editText fields after data is inserted into the database
                emptyEditTextAfterDataInsert();
            }
            else
            {
                // Display toast message if any field us empty and focus the field
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     *  Method to insert the Jersey into the JerseyDatabase
     */
    public void insertUserIntoDatabase()
    {
        String username = UsernameHolder.getText().toString().trim();
        String password = PasswordHolder.getText().toString().trim();

        User user = new User(username, password);
        handler.createUser(user);

        // Display message of successful register
        Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();

        // Start LoginActivity after successful register
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        this.finish();
    }

    /**
     * Method to check if the username and password fields are empty
     * @return A String message based on if the username or password fields are empty
     */
    public String checkEditTextNotEmpty()
    {
        String message = "";
        String username = UsernameHolder.getText().toString().trim();
        String password = PasswordHolder.getText().toString().trim();

        if(username.isEmpty())
        {
            UsernameHolder.requestFocus();
            EmptyHolder = true;
            message = "Username is Empty";
        }
        else if(password.isEmpty())
        {
            PasswordHolder.requestFocus();
            EmptyHolder = true;
            message = "Password is Empty";
        }
        else
        {
            EmptyHolder = false;
        }
        return message;
    }

    /**
     * Check if the username already exists within the UserDatabase
     */
    public void checkUsernameAlreadyExists()
    {
        String username = UsernameHolder.getText().toString().trim();
        database = handler.getWritableDatabase();

        // Add username to the cursor
        Cursor cursor = database.query(UserDatabaseHandler.TABLE_NAME, null, " " +
        UserDatabaseHandler.COLUMN_1_USERNAME + " = ?", new String[]{username},
        null, null, null);

        while (cursor.moveToNext())
        {
            if (cursor.isFirst())
            {
                // Determine if username exists
                cursor.moveToFirst();
                // Set result variable to Username Found
                F_Result = "Username Found";

                cursor.close();
            }
        }
        handler.close();

        // Call method to check the result and insert of the user into the UserDatabase
        checkFinalCredentials();
    }

    /**
     * Method to check if the username already exists within the UserDatabse
     */
    public void checkFinalCredentials()
    {
        if(F_Result.equalsIgnoreCase("Username Found"))
        {
            Toast.makeText(RegisterActivity.this, "Username Already Exists", Toast.LENGTH_LONG).show();
        }
        else
        {
            insertUserIntoDatabase();
        }
        F_Result = "Not_Found";
    }

    /**
     * Clear editText fields after insertion into the UserDatabase
     */
    public void emptyEditTextAfterDataInsert()
    {
        UsernameHolder.getText().clear();
        PasswordHolder.getText().clear();
    }

}

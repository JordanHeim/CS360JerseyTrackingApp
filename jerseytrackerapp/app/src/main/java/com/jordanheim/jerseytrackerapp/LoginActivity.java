package com.jordanheim.jerseytrackerapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Class to display the login screen and add functionality
 * to the various components within
 */
public class LoginActivity extends AppCompatActivity
{
    Activity activity;

    Button LoginBtn, RegisterBtn;
    EditText Username, Password;
    String UsernameHolder, PasswordHolder;
    Boolean EmptyHolder;
    PopupWindow popupWindow;
    SQLiteDatabase database;
    UserDatabaseHandler handler;
    String TempPassword = "NOT_FOUND";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        activity = this;

        // Initialize buttons, editTexts, and database handler
        LoginBtn = findViewById(R.id.loginBtn);
        RegisterBtn = findViewById(R.id.signUpBtn);
        Username = findViewById(R.id.usernameEditText);
        Password = findViewById(R.id.passwordEditText);
        handler = new UserDatabaseHandler(this);

        // Add click listener to loginBtn
        LoginBtn.setOnClickListener(view -> {
            loginFunction();
        });
        // Add click listener to RegisterBtn
        RegisterBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Method that is used to login the user
     * Checks the password against the username in the UserDatabase
     * Will return message based on success or failure of the search
     */
    public void loginFunction()
    {
        String message = checkEditTextNotEmpty();

        if(!EmptyHolder)
        {
            database = handler.getWritableDatabase();

            //Get the username and add it to the cursor
            Cursor cursor = database.query(UserDatabaseHandler.TABLE_NAME, null, " " +
                    UserDatabaseHandler.COLUMN_1_USERNAME + " = ?", new String[]{UsernameHolder},
                    null, null, null);

            while (cursor.moveToNext())
            {
                if (cursor.isFirst())
                {
                    cursor.moveToFirst();

                    // Store password associated with the username
                    TempPassword = cursor.getString(cursor.getColumnIndex(UserDatabaseHandler.COLUMN_2_PASSWORD));
                    cursor.close();
                }
            }
            handler.close();

            // Call the checkFinalResult method
            checkFinalResult();
        }
        else
        {
            // Will show a message if Login editText fields are empty
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to check if the EditText fields are empty within the login screen
     * @return A String message that either the username or password is empty
     */
    public String checkEditTextNotEmpty()
    {
        String message = "";
        UsernameHolder = Username.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();

        if (UsernameHolder.isEmpty())
        {
            Username.requestFocus();
            EmptyHolder = true;
            message = "Username is Empty";
        }
        else if (PasswordHolder.isEmpty())
        {
            Password.requestFocus();
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
     * Method to compare the entered password against the username's database
     * associated password
     */
    public void checkFinalResult()
    {
        if (TempPassword.equalsIgnoreCase(PasswordHolder))
        {
            // Show a message that the login was successful
            Toast.makeText(LoginActivity.this, "Logging in!", Toast.LENGTH_SHORT).show();

            // Pass the username to JerseyListActivity
            Bundle bundle = new Bundle();
            bundle.putString("user_name", UsernameHolder);

            // Go to JerseyListActivity after successful login
            Intent intent = new Intent(LoginActivity.this, JerseyListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

            // Clear the editText fields once login is successful
            emptyEditTextAfterDataInsert();
        }
        else
        {
            // Display the error message when incorrect credentials are entered
            Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
        }
        TempPassword = "NOT_FOUND";
    }

    /**
     * Method to clear the editText fields on the login screen
     */
    public void emptyEditTextAfterDataInsert()
    {
        Username.getText().clear();
        Password.getText().clear();
    }


}

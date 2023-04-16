package com.jordanheim.jerseytrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to implement the functionality of adding a Jersey object
 */
public class AddJerseyActivity extends AppCompatActivity
{
    String JerseyNameHolder;
    String JerseyTypeHolder;
    String JerseyQuantityHolder;
    String usernameHolder;

    EditText JerseyName;
    EditText JerseyType;
    EditText JerseyQuantity;

    Button AddJerseyBtn;

    Boolean EmptyHolder;

    JerseyDatabaseHandler database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_add_jersey);

        // Initialize editText, textViews, and buttons
        JerseyName = findViewById(R.id.editTextJerseyName);
        JerseyType = findViewById(R.id.editTextJerseyType);
        JerseyQuantity = findViewById(R.id.editTextJerseyCount);
        AddJerseyBtn = findViewById(R.id.addJerseyButton);
        database = new JerseyDatabaseHandler(this);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        // Get username from JerseyListActivity
        usernameHolder = intent.get().getStringExtra(JerseyListActivity.usernameHolder);

        // Add click listener to AddJerseyButton
        AddJerseyBtn.setOnClickListener(view -> insertJerseyIntoDatabase());
    }

    /**
     * Method used to insert Jersey object into the Jersey Database
     * Will return a toast message on the success of jersey added
     */
    public void insertJerseyIntoDatabase()
    {
        String message = checkEditTextNotEmpty();

        if (!EmptyHolder)
        {
            String jerseyName = JerseyNameHolder;
            String jerseyType = JerseyTypeHolder;
            String jerseyQuantity = JerseyQuantityHolder;
            String username = usernameHolder;

                Jersey jersey = new Jersey(username, jerseyName, jerseyType, jerseyQuantity);
                database.createJersey(jersey);

                Toast.makeText(this, "Jersey Added!", Toast.LENGTH_LONG).show();

                Intent add = new Intent();
                setResult(RESULT_OK, add);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Jersey Name already used", Toast.LENGTH_LONG).show();
            }
    }

    /**
     * Check to make sure that JerseyName and Type are not empty
     * @return String message regarding the status of the jersey editText field
     */
    public String checkEditTextNotEmpty()
    {
        String message = "";
        JerseyNameHolder = JerseyName.getText().toString().trim();
        JerseyTypeHolder = JerseyType.getText().toString().trim();
        JerseyQuantityHolder = JerseyQuantity.getText().toString().trim();
        
        if (JerseyNameHolder.isEmpty())
        {
            JerseyName.requestFocus();
            EmptyHolder = true;
            message = "Jersey Name is Empty";
        }
        else if (JerseyTypeHolder.isEmpty())
        {
            JerseyName.requestFocus();
            EmptyHolder = true;
            message = "Jersey Type is Empty";
        }
        else
        {
            EmptyHolder = false;
        }
        return message;
    }


}

package com.jordanheim.jerseytrackerapp;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Class that populates the main Jersey viewing screen and add functionality
 * to the various components within
 */
public class JerseyListActivity extends AppCompatActivity
{
    ImageButton AddJerseyBtn, SMSBtn;
    ListView JerseysListView;
    JerseyDatabaseHandler database;
    AlertDialog alertDialog = null;
    ArrayList<Jersey> jerseys;
    JerseyItemsList jerseyItemsList;
    int jerseysCount;

    static String usernameHolder;

    private static final int USER_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static boolean smsAuthorized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jerseys);

        // Initialize buttons, textViews, listViews, and database
        AddJerseyBtn = findViewById(R.id.addJerseyButton);
        SMSBtn = findViewById(R.id.SMSButton);
        JerseysListView = findViewById(R.id.bodyListView);
        database = new JerseyDatabaseHandler(this);

        // Get the username from the loginActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            usernameHolder = bundle.getString("username");
        }

        jerseys = (ArrayList<Jersey>) database.getAllJerseys();

        jerseysCount = database.getJerseysCount();

        if (jerseysCount > 0)
        {
            jerseyItemsList = new JerseyItemsList(this, jerseys, database);
            JerseysListView.setAdapter(jerseyItemsList);
        }
        else
        {
            Toast.makeText(this, "Database is Empty", Toast.LENGTH_LONG).show();
        }


        // Add click listener to addJerseyBtn
        AddJerseyBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddJerseyActivity.class);
            startActivityForResult(intent, 1);
        });

        // Add click listener to SMSBtn
        SMSBtn.setOnClickListener(view -> {

            // Get SMS permission from the user
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
                {
                    Toast.makeText(this, "Need SMN Permission", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, USER_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
            else
            {
                Toast.makeText(this, "SMS Permission", Toast.LENGTH_LONG).show();
            }

            // Open the alert Dialog for SMS Alert
            alertDialog = TextNotificationAlert.doubleButton(this);
            alertDialog.show();

        });
    }

    /**
     *  Method that is called when an activity that us launched exits, giving you the requestCode you
     *  started it with, the resultCode it returned, and any additional data from it
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                jerseysCount = database.getJerseysCount();

                if(jerseyItemsList == null)
                {
                    jerseyItemsList = new JerseyItemsList(this, jerseys, database);
                    JerseysListView.setAdapter(jerseyItemsList);
                }

                JerseyItemsList.jerseys = (ArrayList<Jersey>) database.getAllJerseys();
                ((BaseAdapter)JerseysListView.getAdapter()).notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Method to set smsAuthorized to true that allows SMS messages to be sent
     */
    public static void allowSendSMS()
    {
        smsAuthorized = true;
    }

    /**
     * Method to set smsAuthorized to false and deny SMS messages to be sent
     */
    public static void denySendSMS()
    {
        smsAuthorized = false;
    }

    /**
     * Method to send SMS message if a jersey quantity reaches 0
     * @param context - the context
     */
    public static void sendSMSMessage(Context context)
    {
        String smsMsg = "You currently have one or more jerseys at quantity of 0";

        if (smsAuthorized)
        {
            try
            {
                Toast.makeText(context, smsMsg, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(context, "SMS Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

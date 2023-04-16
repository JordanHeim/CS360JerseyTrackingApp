package com.jordanheim.jerseytrackerapp;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class JerseyItemsList extends BaseAdapter
{
    private final Activity context;
    private PopupWindow popupWindow;
    static ArrayList<Jersey> jerseys;
    JerseyDatabaseHandler database;

    public JerseyItemsList(Activity context, ArrayList<Jersey> jerseys, JerseyDatabaseHandler database)
    {
        this.context = context;
        this.jerseys = jerseys;
        this.database = database;
    }


    public static class ViewHolder
    {
        TextView textViewJerseyId;
        TextView textViewJerseyName;
        TextView textViewJerseyType;
        TextView textViewJerseyQty;
        ImageButton editBtn;
        ImageButton deleteBtn;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder viewHolder;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.jersey_row, null, true);

            viewHolder.textViewJerseyId = row.findViewById(R.id.textViewJerseyIdRow);
            viewHolder.textViewJerseyName = row.findViewById(R.id.textViewJerseyNameRow);
            viewHolder.textViewJerseyType = row.findViewById(R.id.textViewJerseyTypeRow);
            viewHolder.textViewJerseyQty = row.findViewById(R.id.textViewJerseyQuantityRow);
            viewHolder.editBtn = row.findViewById(R.id.editButtonRow);
            viewHolder.deleteBtn = row.findViewById(R.id.deleteButtonRow);

            row.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textViewJerseyId.setText("" + jerseys.get(pos).getId());
        viewHolder.textViewJerseyName.setText(jerseys.get(pos).getJerseyName());
        viewHolder.textViewJerseyType.setText(jerseys.get(pos).getJerseyType());
        viewHolder.textViewJerseyQty.setText(jerseys.get(pos).getJerseyQty());

        // Check the Jersey quantity and determine if an SMS needs to be sent
        String value = viewHolder.textViewJerseyQty.getText().toString().trim();
        if (value.equals("0"))
        {
            // Change the background color and text color of the Jersey
            // quantity if the value is 0
            viewHolder.textViewJerseyQty.setBackgroundColor(Color.RED);
            viewHolder.textViewJerseyQty.setTextColor(Color.WHITE);
            JerseyListActivity.sendSMSMessage(context.getApplicationContext());
        }
        else
        {
            // Change the background color and text color of the Jersey
            // quantity back to default if not 0
            viewHolder.textViewJerseyQty.setBackgroundColor(Color.parseColor("#C1E8FF"));
            viewHolder.textViewJerseyQty.setTextColor(Color.BLACK);
        }

        final int posPopup = pos;

        viewHolder.editBtn.setOnClickListener(view -> editPopup(posPopup));

        viewHolder.deleteBtn.setOnClickListener(view -> {
            database.deleteJersey(jerseys.get(posPopup));

            jerseys = (ArrayList<Jersey>) database.getAllJerseys();
            notifyDataSetChanged();

            Toast.makeText(context, "Jersey Deleted", Toast.LENGTH_SHORT).show();
        });

        return row;
    }

    public Object getItem(int pos)
    {
        return pos;
    }
    public long getItemId(int pos)
    {
        return pos;
    }
    public int getCount()
    {
        return jerseys.size();
    }

    /**
     * Get the popup edit jersey window to edit the selected Jersey quantity
     * @param posPopup - get the position of the Jersey that needs to be edited
     */
    public void editPopup(final int posPopup)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.popup_edit_jersey, context.findViewById(R.id.popup_element));

        popupWindow = new PopupWindow(layout, 800, 1000, true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText editJerseyQuantity = layout.findViewById(R.id.editTextItemQtyPopup);

        final TextView textViewJerseyName = layout.findViewById(R.id.textViewEditJerseyName);

        textViewJerseyName.setText(jerseys.get(posPopup).getJerseyName());
        editJerseyQuantity.setText(jerseys.get(posPopup).getJerseyQty());

        Button update = layout.findViewById(R.id.updateButton);

        update.setOnClickListener(view -> {
            String jerseyQty = editJerseyQuantity.getText().toString();

        Jersey jersey = jerseys.get(posPopup);
        jersey.setJerseyQty(jerseyQty);

        database.updateJersey(jersey);
        jerseys = (ArrayList<Jersey>) database.getAllJerseys();
        notifyDataSetChanged();

        Toast.makeText(context, "Jersey Updated", Toast.LENGTH_SHORT).show();
        });
    }

}

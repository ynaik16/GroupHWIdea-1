package com.example.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

//Adapter for the Contact list
public class ContactAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<ContactInfo> values;

    //constructor
    public ContactAdapter(Context context, ArrayList<ContactInfo> list) {
        super(context, R.layout.custom_layout, list);
        this.context = context;
        this.values = list;
    }


    //gets called from MainActivity to populate the ListView with given items
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.custom_layout, parent, false);

        TextView tvContactName = myView.findViewById(R.id.tvContactName);
        TextView tvContactPhone = myView.findViewById(R.id.tvContactPhone);
        TextView tvContactEmail = myView.findViewById(R.id.tvContactEmail);

        tvContactName.setText(values.get(position).getName());
        tvContactPhone.setText(values.get(position).getPhone());
        tvContactEmail.setText((values.get(position).getEmail()));

        return myView;
    }
}

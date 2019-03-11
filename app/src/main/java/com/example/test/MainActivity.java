package com.example.test;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    Button btnAddContact;
    ListView lvContacts;
    ArrayList<ContactInfo> list;

    //test button
    ImageButton btnTest;

    //variables to hold values
    String name = "", phone = "", email = "";

    //code to use when using startActivity()
    final int ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.imageButton);

        //references to the components on the app
        btnAddContact = findViewById(R.id.btnXMLAddContact);
        lvContacts = findViewById(R.id.lvContacts);

        //make lvContacts an onClickListener


        //holds ContactInfo (name,phone, email)
        list = new ArrayList<ContactInfo>();

        //Called when the 'Add Contact' button is clicked/tapped
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent when you set up to go to a new activity
                Intent toAddContact = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(toAddContact, ADD_CONTACT);
            }
        });

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* TODO * When we click the item in the list view we open a popup window asking
                 * TODO * you want to call or email the selected person */
                Toast.makeText(MainActivity.this,"You clicked position " + i , Toast.LENGTH_LONG).show();
            }
        });

        //button to open app given through the pass string
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String pass = "tel:5104984470";
                //String pass = "https://google.com
                String pass = "geo:50.123,7.1434?z=19";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(pass));
                startActivity(intent);
            }
        });
    }

    //Called when you successfully return from your Intent/startActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //If you used the requestCode and successfully returned
        if (requestCode == ADD_CONTACT){
            if (resultCode == RESULT_OK){
                //store data
                name = data.getStringExtra("name");
                phone = data.getStringExtra("phone");
                email = data.getStringExtra("email");

                //create a new ContactInfo with all these attributes and add to the list
                ContactInfo newContact = new ContactInfo(name,phone,email);
                list.add(newContact);

                //Create an adapter to speak to the ListView
                //Pass context of this page and the list populated with returned Intent data
                ContactAdapter adapter = new ContactAdapter(this, list);

                //calls the getView
                lvContacts.setAdapter(adapter);
            }
        }
    }


}

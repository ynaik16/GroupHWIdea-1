package com.example.test;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    //Components
    public static final String SUPPORT = "Helpline Number: 1800 885 4390";
    Button btnAddContact;
    ListView lvContacts;
    ArrayList<ContactInfo> list;

    //variables to hold values
    String name = "", phone = "", email = "";

    //code to use when using startActivity()
    final int ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Incomplete/ Test variables */
        final Dialog  MyDialog = new Dialog(this);

        /* References to the components on the app */
        btnAddContact = findViewById(R.id.btnXMLAddContact);
        lvContacts = findViewById(R.id.lvContacts);

        /* Holds ContactInfo (name,phone, email) */
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

        //Called when a contact is clicked
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* In words saying 'in the list of contacts, get the array position(i), and return whatever
                * Basically want o use these to pass into an Intent*/
                String conName = list.get(i).getName();
                final String conPhone = list.get(i).getPhone();
                final String conEmail = list.get(i).getEmail();

                /* Make sure you set the view before you find the buttons */
                MyDialog.setContentView(R.layout.popup_layout);

                /* Find the button from that popup_layout.xml */
                ImageButton conBtnPhone = MyDialog.findViewById(R.id.btnPopupCall);
                ImageButton conBtnEmail = MyDialog.findViewById(R.id.btnPopupEmail);


                conBtnPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("tel:" + conPhone));
                        startActivity(intent);
                    }
                });

                conBtnEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //start an intent to send an email
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mailto:" + conEmail));
                        startActivity(intent);
                    }
                });
                MyDialog.show();

                Toast.makeText(MainActivity.this,"You clicked on: " + conName , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public void displayToastMsg(View v){
        toastMsg(SUPPORT);
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

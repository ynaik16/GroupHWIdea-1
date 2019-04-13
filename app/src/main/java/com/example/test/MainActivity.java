package com.example.test;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    //Components
    private GoogleSignInClient mGoogleSignInClient;
    Button btnAddContact;
    ListView lvContacts;
    ArrayList<ContactInfo> list;
    DatabaseHelper myDb;
    TextView contactsCount;
    Button btnSignOut;

    //variables to hold values
    public static final String SUPPORT = "Helpline Number: 1800 885 4390";
    String name = "", phone = "", email = "";
    int itemID, contactCounter;

    //code to use when using startActivity()
    final int ADD_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Incomplete/ Test variables */
        final Dialog MyDialog = new Dialog(this);

        /* References to the components on the app */
        btnAddContact = findViewById(R.id.btnXMLAddContact);
        btnSignOut = findViewById(R.id.btnXMLSignOut);
        lvContacts = findViewById(R.id.lvContacts);
        contactsCount = findViewById(R.id.textView);

        /* Holds ContactInfo (name,phone, email) */
        list = new ArrayList<ContactInfo>();

        /* Use Database to load in on the initialization of app */
        myDb = new DatabaseHelper(this);
        Cursor data = myDb.getListContents();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        if(data.getCount() > 0) {
            while(data.moveToNext()) {
                //get the string in each column and add it in a new ContactInfo person
                String dataName = data.getString(1);
                String dataPhone = data.getString(2);
                String dataEmail = data.getString(3);
                ContactInfo person = new ContactInfo(dataName,dataPhone,dataEmail);

                //add it to the list
                list.add(person);
                ContactAdapter adapter = new ContactAdapter(this, list);
                lvContacts.setAdapter(adapter);
            }
        }
        contactCounter = data.getCount();
        contactsCount.setText("Contacts " + contactCounter);

        //Called when the 'Add Contact' button is clicked/tapped
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent when you set up to go to a new activity
                Intent toAddContact = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(toAddContact, ADD_CONTACT);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        //Called when a contact is clicked
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* In words saying 'in the list of contacts, get the array position(i), and return whatever
                 * Basically want o use these to pass into an Intent*/
                final String conName = list.get(i).getName();
                final String conPhone = list.get(i).getPhone();
                final String conEmail = list.get(i).getEmail();

                /* Make sure you set the view before you find the buttons */
                MyDialog.setContentView(R.layout.popup_layout);

                /* Setup the buttons and textviews in the popup */
                TextView contvName = MyDialog.findViewById(R.id.tvPopupName);
                TextView contvPhone = MyDialog.findViewById(R.id.tvPopupNumber);
                TextView contvEmail = MyDialog.findViewById(R.id.tvPopupAddress);
                ImageButton conBtnPhone = MyDialog.findViewById(R.id.btnPopupCall);
                ImageButton conBtnEmail = MyDialog.findViewById(R.id.btnPopupEmail);
                Button conBtnDelete = MyDialog.findViewById(R.id.btnPopupDelete);


                /* Set the textview to the selected users info */
                contvName.setText(conName);
                contvPhone.setText(conPhone);
                contvEmail.setText(conEmail);

                //We check email because emails are generally unique
                Cursor data = myDb.getItemID(conEmail);
                itemID = -1; // ID will be -1 by default
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if (itemID < 0){
                    toastMsg("No ID associated");
                }

                // Call button listener
                conBtnPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("tel:" +conPhone));
                        startActivity(intent);
                    }
                });

                // Email button listener
                conBtnEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //start an intent to send an email
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mailto:" + conEmail));
                        startActivity(intent);
                    }
                });

                // Delete button listener
                conBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        myDb.deleteItem(itemID,conName,conPhone,conEmail);

                        //this acts like a refresh to the view, may have to change with addition of login
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        setContactCount(-1);
                    }
                });
                MyDialog.show();
            }
        });
    }

    // Easier toast
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

                //for database
                AddData(name,phone,email);

                //Create an adapter to speak to the ListView
                //Pass context of this page and the list populated with returned Intent data
                ContactAdapter adapter = new ContactAdapter(this, list);

                //calls the getView
                lvContacts.setAdapter(adapter);
                setContactCount(1);
            }
        }
    }

    // enters item into the database
    public void AddData(String item1, String item2, String item3){
        boolean insertData = myDb.addData(item1,item2,item3);

        if (insertData == false)
            Toast.makeText(MainActivity.this,"Failed to enter data to database" , Toast.LENGTH_LONG).show();
    }

    //set the text view to show how many contacts
    public void setContactCount(int num) {
        contactCounter += num;
        contactsCount.setText("Contacts " + contactCounter);
    }

    //handle sign out
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();   //finish will exit to the sign in screen
                    }
                });
    }
}
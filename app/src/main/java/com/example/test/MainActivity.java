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
    String name = "", phone = "", email = "", address = "";
    int itemID, contactCounter;

    //code to use when using startActivityForResult()
    final int ADD_CONTACT = 1;
    final int EDIT_CONTACT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* **************************************************************** */
        /* ***************************** SET UP  ************************** */
        /* **************************************************************** */

        /* For the popup when clicking a listView item(contact) */
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

        /* Google Sign In stuff */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /* ********************************************** */
        /* ***************** LOAD DATA  ***************** */
        /* ********************************************** */
        if(data.getCount() > 0) {
            while(data.moveToNext()) {

                //get the string in each column and add it in a new ContactInfo person
                String dataName = data.getString(1);
                String dataPhone = data.getString(2);
                String dataEmail = data.getString(3);
                String dataAddress = data.getString(4);
                ContactInfo person = new ContactInfo(dataName,dataPhone,dataEmail,dataAddress);

                //add it to the list
                list.add(person);
                ContactAdapter adapter = new ContactAdapter(this, list);
                lvContacts.setAdapter(adapter);
            }
        }
        /* Will add a counter to how many contacts there are */
        contactCounter = data.getCount();
        contactsCount.setText("Contacts " + contactCounter);

        /* ****************************************** */
        /* ***************** BUTTONS  *************** */
        /* ****************************************** */

        /* Add Contact */
        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent when you set up to go to a new activity
                Intent toAddContact = new Intent(MainActivity.this, AddContact.class);
                startActivityForResult(toAddContact, ADD_CONTACT);
            }
        });

        /* Sign Out */
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        /* ******************** */
        /* *** POPUP WINDOW *** */
        /* ******************** */

        /* Tap/Click a contact in the listView */
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /* In words saying 'in the list of contacts, get the array position(i), and return whatever
                 * Basically want o use these to pass into an Intent*/
                final String conName = list.get(i).getName();
                final String conPhone = list.get(i).getPhone();
                final String conEmail = list.get(i).getEmail();
                final String conAddress = list.get(i).getAddress();

                /* Make sure you set the view before you find the buttons */
                MyDialog.setContentView(R.layout.popup_layout);

                /* Setup the buttons and textviews in the popup */
                TextView contvName = MyDialog.findViewById(R.id.tvPopupName);
                TextView contvPhone = MyDialog.findViewById(R.id.tvPopupNumber);
                TextView contvEmail = MyDialog.findViewById(R.id.tvPopupAddress);
                ImageButton conBtnPhone = MyDialog.findViewById(R.id.btnPopupCall);
                ImageButton conBtnEmail = MyDialog.findViewById(R.id.btnPopupEmail);
                ImageButton conBtnGeo = MyDialog.findViewById(R.id.btnPopupLocation);
                Button conBtnDelete = MyDialog.findViewById(R.id.btnPopupDelete);
                Button conBtnEdit = MyDialog.findViewById(R.id.btnPopupEdit);


                /* Set the textview to the selected users info */
                contvName.setText(conName);
                contvPhone.setText(conPhone);
                contvEmail.setText(conEmail);

                /* Will grab the database ID for the item you clicked */
                /* Use Email because the email is the most unique field */
                Cursor data = myDb.getItemID(conEmail);
                itemID = -1; // ID will be -1 by default
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if (itemID < 0){
                    toastMsg("No ID associated");
                }

                /* Popup Call Button */
                conBtnPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("tel:" +conPhone));
                        startActivity(intent);
                    }
                });

                /* Popup Email Button */
                conBtnEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //start an intent to send an email
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mailto:" + conEmail));
                        startActivity(intent);
                    }
                });

                /* Popup Geo Button */
                conBtnGeo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO change to contacts address
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("geo:0,0?q=" + conAddress.replaceAll("\\s+","+")));
                        startActivity(intent);
                    }
                });

                /* Popup Delete Button */
                conBtnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        myDb.deleteItem(itemID,conName,conPhone,conEmail,conAddress);
                        refresh();
                        setContactCount(-1);
                    }
                });

                /* Popup Edit Button */
                conBtnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(MainActivity.this,AddContact.class);
                        editIntent.putExtra("id",itemID);
                        editIntent.putExtra("name",conName);
                        editIntent.putExtra("phone",conPhone);
                        editIntent.putExtra("email",conEmail);
                        editIntent.putExtra("address",conAddress);
                        startActivityForResult(editIntent, EDIT_CONTACT);
                    }
                });

                /* Show the popup */
                MyDialog.show();
            }
        });
    }

    /* Called when returning from a startActivityForResult */
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
                address = data.getStringExtra("address");

                //create a new ContactInfo with all these attributes and add to the list
                ContactInfo newContact = new ContactInfo(name,phone,email,address);
                list.add(newContact);

                //for database
                AddData(name,phone,email,address);

                //Create an adapter to speak to the ListView
                //Pass context of this page and the list populated with returned Intent data
                ContactAdapter adapter = new ContactAdapter(this, list);

                //calls the getView
                lvContacts.setAdapter(adapter);
                setContactCount(1);
            }
        }

        //If we returned using the requestCode to edit
        else if (requestCode == EDIT_CONTACT) {
            if (resultCode == RESULT_OK) {
                refresh();
            }
        }
    }

    /* **************************************************************** */
    /* **************************** UTILITY  ************************** */
    /* **************************************************************** */

    /* Make a Toast Message appear*/
    public void toastMsg(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /* Used when clicking the caution button at the top of the main layout */
    public void displayToastMsg(View v){
        toastMsg(SUPPORT);
    }

    /* Will Refresh the layout */
    public void refresh() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    /* Add Data using the Database*/
    public void AddData(String item1, String item2, String item3,String item4){
        boolean insertData = myDb.addData(item1,item2,item3,item4);

        if (insertData == false)
            Toast.makeText(MainActivity.this,"Failed to enter data to database" , Toast.LENGTH_LONG).show();
    }

    /* Set the count (only pass 1 or -1) */
    public void setContactCount(int num) {
        contactCounter += num;
        contactsCount.setText("Contacts " + contactCounter);
    }

    /* Sign Out Handler*/
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
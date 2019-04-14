package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddContact extends AppCompatActivity {
    //References towards layout buttons/edittext's
    Button btnConfirm;
    EditText etName, etPhone, etEmail, etAddress;

    //Database helper
    DatabaseHelper myDb;

    // Strings to hold editIntent values
    private String selectName, selectPhone, selectEmail, selectAddress;
    private int selectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);   //new page
        myDb = new DatabaseHelper(this); //initialize database helper

        //Ties the variable name to the button/edittext id
        btnConfirm = findViewById(R.id.btnXMLConfirm);
        etName = findViewById(R.id.editTextName);
        etPhone = findViewById(R.id.editTextPhone);
        etEmail = findViewById(R.id.editTextEmail);
        etAddress = findViewById(R.id.editTextAddress);

        /* If we intented with an edit we can get that info here */
        Intent receiveIntent = getIntent();
        if (receiveIntent != null) {
            selectID = receiveIntent.getIntExtra("id", -1);
            selectName = receiveIntent.getStringExtra("name");
            selectPhone = receiveIntent.getStringExtra("phone");
            selectEmail = receiveIntent.getStringExtra("email");
            selectAddress = receiveIntent.getStringExtra("address");
            fillText(); //fill in the editText with already existing data (helps user know it's an edit)
        }


        //called when confirm button is clicked/tapped
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //error checking if input boxes aren't filled
                if (etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty()
                    || etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(AddContact.this,"Fill all fields", Toast.LENGTH_LONG).show();
                }

                //If our ID is not -1 then we're in this class to edit
                else if (selectID != -1) {
                    //set up an array so we don't have a clunky function to call
                    ArrayList<String> newData = new ArrayList<>();
                    fillArray(newData);

                    //call the updateContact in our database
                    myDb.updateContact(newData, selectID);

                    //result is success and close
                    setResult(RESULT_OK);
                    AddContact.this.finish();
                }

                //this will happen when we're just plainly adding a contact
                else{
                    //set up to move to a new page
                    Intent toMain = new Intent();

                    //include extra when moving back
                    toMain.putExtra("name", etName.getText().toString().trim());
                    toMain.putExtra("phone",etPhone.getText().toString().trim());
                    toMain.putExtra("email",etEmail.getText().toString().trim());
                    toMain.putExtra("address",etAddress.getText().toString().trim());

                    //returns the success code and intent data
                    setResult(RESULT_OK, toMain);

                    //close this page
                    AddContact.this.finish();
                }
            }
        });
    }

    //quick function to fill array with contact data
    public ArrayList<String> fillArray(ArrayList<String> newData){
        newData.add(etName.getText().toString());
        newData.add(etPhone.getText().toString());
        newData.add(etEmail.getText().toString());
        newData.add(etAddress.getText().toString());

        return newData;
    }

    //function to fill edittext with contact data
    public void fillText() {
        etName.setText(selectName);
        etPhone.setText(selectPhone);
        etEmail.setText(selectEmail);
        etAddress.setText(selectAddress);
    }
}

package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContact extends AppCompatActivity {
    //References towards layout buttons/edittext's
    Button btnConfirm;
    EditText etName, etPhone, etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);   //new page

        //Ties the variable name to the button/edittext id
        btnConfirm = findViewById(R.id.btnXMLConfirm);
        etName = findViewById(R.id.editTextName);
        etPhone = findViewById(R.id.editTextPhone);
        etEmail = findViewById(R.id.editTextEmail);

        //called when confirm button is clicked/tapped
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //error checking if input boxes aren't filled
                if (etName.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty()
                    || etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(AddContact.this,"Fill all fields", Toast.LENGTH_LONG).show();
                } else{
                    //set up to move to a new page
                    Intent toMain = new Intent();

                    //include extra when moving back
                    toMain.putExtra("name", etName.getText().toString().trim());
                    toMain.putExtra("phone",etPhone.getText().toString().trim());
                    toMain.putExtra("email",etEmail.getText().toString().trim());

                    //returns the success code and intent data
                    setResult(RESULT_OK, toMain);

                    //close this page
                    AddContact.this.finish();
                }
            }
        });
    }

}

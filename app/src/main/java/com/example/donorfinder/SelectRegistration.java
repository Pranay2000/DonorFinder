package com.example.donorfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SelectRegistration extends AppCompatActivity {

    private Button donorButton,recipientButton;
    private TextView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_registration);

        recipientButton=findViewById(R.id.recipientbutton);
        donorButton=findViewById(R.id.donorbutton);
        backButton=findViewById(R.id.backbutton);


        recipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectRegistration.this, RecipientRegistration.class);
                startActivity(intent);
            }
        });

        donorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectRegistration.this, DonorRegistration.class);
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SelectRegistration.this, login.class);
                startActivity(intent);
            }
        });
    }
}
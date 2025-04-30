package com.example.truckingappmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_login);

        // Find the Login button by its ID
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCustomerSignup = findViewById(R.id.btnSignInCustomer);

        // Set OnClickListener for the Login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the Login button is clicked, start LoginActivity
                Intent intent = new Intent(MainPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnCustomerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainPageActivity.this, CustomerSignUpActivity.class);
                startActivity(intent);
            }
        });
    }
}

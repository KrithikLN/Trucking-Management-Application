package com.example.truckingappmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ImageView imageViewTop;
    private TextView dataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        imageViewTop = findViewById(R.id.imageViewTop);

        // Set click listener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve username and password from EditText fields
                String username = editTextName.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validate username and password
                if (!username.isEmpty() && !password.isEmpty()) {
                    // Perform login validation in the background
                    new LoginTask().execute(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set image resource for the top image view
        imageViewTop.setImageResource(R.drawable.applogo);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String enteredPassword = params[1];

            // Example password validation (you can customize this according to your requirements)
            boolean isValidPassword = isPasswordValid(enteredPassword);

            // You may add more complex validation logic here

            return isValidPassword;
        }

        @Override
        protected void onPostExecute(Boolean isValid) {
            if (isValid) {
                // Successful login
                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                // Move to another activity (replace NextActivity.class with the desired activity)
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // This will finish the current activity to prevent going back with the back button
            } else {
                // Invalid credentials
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }

        // Example password validation function
        private boolean isPasswordValid(String password) {
            // Customize the validation rules as needed
            return password.length() >= 8 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*");
        }
    }
}


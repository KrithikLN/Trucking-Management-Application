package com.example.truckingappmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerSignUpActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView imageView;
    private Bitmap photoBitmap;

    private ImageUploadService imageUploadService;

    private EditText editTextName;
    private EditText editTextPhoneNumber;
    private EditText editTextEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_signup);

        // Initialize UI components
        imageView = findViewById(R.id.imageView);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);

        // Initialize Retrofit for image upload
        Retrofit retrofitImage = new Retrofit.Builder()
                .baseUrl("http://192.168.33.217:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the service for image upload
        imageUploadService = retrofitImage.create(ImageUploadService.class);

        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);
        btnUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open image picker
                pickImage();
            }
        });

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoBitmap != null) {
                    uploadImageAndData();
                } else {
                    Toast.makeText(CustomerSignUpActivity.this, "Please upload a photo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pickImage() {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhotoIntent.setType("image/*");
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    photoBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    imageView.setImageBitmap(photoBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadImageAndData() {
        // Convert Bitmap to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // Create a RequestBody from the byte array
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        // Create a MultipartBody.Part from the RequestBody
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", "image.jpg", requestBody);

        // Create RequestBody for text data
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), editTextName.getText().toString().trim());
        RequestBody phoneNumber = RequestBody.create(MediaType.parse("text/plain"), editTextPhoneNumber.getText().toString().trim());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), editTextEmail.getText().toString().trim());

        // Make the Retrofit call for image upload and data
        Call<Void> call = imageUploadService.uploadImageAndData(imagePart, name, phoneNumber, email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CustomerSignUpActivity.this, "Image and data uploaded successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerSignUpActivity.this, "Failed to upload image and data. Status code: " + response.code(), Toast.LENGTH_SHORT).show();
                    Log.e("UPLOAD", "Failed to upload image and data. Status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CustomerSignUpActivity.this, "Failed to upload image and data", Toast.LENGTH_SHORT).show();
                Log.e("UPLOAD", "Failed to upload image and data", t);
            }
        });
    }
}

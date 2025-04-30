package com.example.truckingappmanager;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageUploadService {

    @Multipart
    @POST("/api/users/signup")
    Call<Void> uploadImageAndData(
            @Part MultipartBody.Part image,
            @Part("name") RequestBody name,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("email") RequestBody email
    );
}

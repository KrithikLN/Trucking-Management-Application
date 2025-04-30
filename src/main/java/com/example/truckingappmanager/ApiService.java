package com.example.truckingappmanager;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    // Adjust the endpoint to match your Spring Boot server's endpoint
    @Multipart
    @POST("/api/images/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}



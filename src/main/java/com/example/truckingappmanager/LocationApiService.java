package com.example.truckingappmanager;// LocationApiService.java
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationApiService {
    private final LocationApi locationApi;
    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;

    public LocationApiService(Context context) {
        this.context = context;
        locationApi = RetrofitClientInstance.getRetrofitInstance().create(LocationApi.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public void uploadLocationData() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            String deviceId = getDeviceIMEI(context); // Use your preferred method to get the device ID

                            LocationData locationData = new LocationData(deviceId, location.getLatitude(), location.getLongitude());

                            Log.d("LocationApiService", "JSON Payload: " + new Gson().toJson(locationData));

                            Call<String> call = locationApi.saveLocationData(locationData);
                            call.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    if (response.isSuccessful()) {
                                        Log.d("LocationApi", "Location data uploaded successfully!");
                                    } else {
                                        Log.e("LocationApi", "Failed to upload location data. Status code: " + response.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.e("LocationApi", "Failed to upload location data", t);
                                }
                            });
                        } else {
                            Log.e("LocationApi", "Last known location is null");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("LocationApi", "Error getting last known location", e));
        }
    }

    private String getDeviceIMEI(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}

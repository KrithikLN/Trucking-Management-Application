package com.example.truckingappmanager.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Boolean> showMap;

    public HomeViewModel() {
        mText = new MutableLiveData<>();

        showMap = new MutableLiveData<>();
        // Set the initial value to true if you want to show the map by default
        showMap.setValue(true);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Boolean> getShowMap() {
        return showMap;
    }

    public void setShowMap(boolean shouldShowMap) {
        showMap.setValue(shouldShowMap);
    }
}

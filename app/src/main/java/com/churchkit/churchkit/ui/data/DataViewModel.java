package com.churchkit.churchkit.ui.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DataViewModel extends ViewModel {
    private MutableLiveData<String> users = new MutableLiveData<>();

    public LiveData<String> getUsers() {

        return users;
    }


}

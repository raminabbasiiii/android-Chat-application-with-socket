package com.ramin.chat.ui.login;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.data.network.login.LoginApi;
import com.ramin.chat.model.ContactModel;
import com.ramin.chat.model.LoginModel;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private static final String TAG = "LoginViewModel";
    private LoginApi api;

    @Inject
    public LoginViewModel(LoginApi api) {
        this.api = api;
    }

    public Observable<LoginModel> observeUserLogin(String mobileNumber) {
        return api
                .userLogin(mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}

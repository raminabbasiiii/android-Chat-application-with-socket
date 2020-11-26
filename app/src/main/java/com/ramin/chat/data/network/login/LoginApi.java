package com.ramin.chat.data.network.login;

import com.ramin.chat.model.LoginModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginApi {

    @GET("login.php")
    Observable<LoginModel> userLogin(@Query("mobileNumber") String mobileNumber);
}

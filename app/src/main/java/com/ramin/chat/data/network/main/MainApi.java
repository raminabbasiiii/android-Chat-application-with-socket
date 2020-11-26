package com.ramin.chat.data.network.main;

import com.ramin.chat.model.UserModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainApi {

    @GET("getUser.php")
    Observable<UserModel> getUser(@Query("mobileNumber") String mobileNumber);

}

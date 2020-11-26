package com.ramin.chat.di;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.data.local.AppDatabase;
import com.ramin.chat.util.AppConstants;

import java.net.URISyntaxException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Preferences providePreferences(Context context) {
        return new Preferences(context);
    }

    @Singleton
    @Provides
    static Context provideContext(Application application) {
        return application;
    }

    @Singleton
    @Provides
    static Retrofit providesChatRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static Socket providesSocketClient() {
        Socket socket = null;
        try {
            socket = IO.socket(AppConstants.SOCKET_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return socket;
    }

    @Singleton
    @Provides
    static AppDatabase provideAppDatabase(Context context, @DatabaseInfo String dbName) {
        return Room.databaseBuilder(context,AppDatabase.class,dbName)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @DatabaseInfo
    static String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

}

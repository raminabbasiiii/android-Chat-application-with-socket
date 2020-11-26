package com.ramin.chat.ui.main.chat;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.data.local.AppDatabase;
import com.ramin.chat.model.ChatModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatViewModel extends ViewModel {

    private AppDatabase appDatabase;

    @Inject
    public ChatViewModel( AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public void insertChat(String fromName, String toName, String fromMobileNumber, String toMobileNumber, String message, String time, String date, String direction) {
        ChatModel chatModel = new ChatModel(fromName, toName, fromMobileNumber, toMobileNumber, message, time, date, direction);
        Observable
                .fromCallable(() -> appDatabase.chatDao().insertChat(chatModel))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Observable<List<ChatModel>> observeChats(String fromMobileNumber, String toMobileNumber) {
        return appDatabase
                .chatDao()
                .getChats(fromMobileNumber, toMobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}


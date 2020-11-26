package com.ramin.chat.ui.main.chatlist;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.data.local.AppDatabase;
import com.ramin.chat.model.ChatModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ChatListViewModel extends ViewModel {

    private AppDatabase appDatabase;

    @Inject
    public ChatListViewModel(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    public Observable<List<ChatModel>> observeChatList() {
        return appDatabase
                .chatDao()
                .getChatList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

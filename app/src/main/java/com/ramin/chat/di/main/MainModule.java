package com.ramin.chat.di.main;

import android.os.Handler;

import com.ramin.chat.data.network.main.MainApi;
import com.ramin.chat.ui.main.MainActivity;
import com.ramin.chat.ui.main.chat.MessageAdapter;
import com.ramin.chat.ui.main.chatlist.ChatListAdapter;
import com.ramin.chat.ui.main.chatlist.ChatListItemOnClick;
import com.ramin.chat.ui.main.contacts.ContactsAdapter;
import com.ramin.chat.ui.main.contacts.ContactsListItemOnClick;

import org.json.JSONObject;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MainModule {

    @Provides
    static MainApi provideMainApi(Retrofit retrofit) {
        return retrofit.create(MainApi.class);
    }

    @Provides
    static ContactsAdapter provideContactsAdapter(ContactsListItemOnClick clickListener) {
        return new ContactsAdapter(clickListener);
    }

    @Provides
    static ContactsListItemOnClick provideClickListener(MainActivity activity) {
        return activity;
    }

    @Provides
    static MessageAdapter provideMessageAdapter() {
        return new MessageAdapter();
    }

    @Provides
    static ChatListAdapter provideChatListAdapter(ChatListItemOnClick clickListener) {
        return new ChatListAdapter(clickListener);
    }

    @Provides
    static ChatListItemOnClick provideChatListClickListener(MainActivity activity) {
        return activity;
    }

    @Provides
    static Handler provideHandler() {
        return new Handler();
    }

    @Provides
    static JSONObject provideJSONObject() {
        return new JSONObject();
    }

}

package com.ramin.chat.di.main;

import com.ramin.chat.ui.main.chatlist.ChatListFragment;
import com.ramin.chat.ui.main.chat.ChatFragment;
import com.ramin.chat.ui.main.contacts.ContactsFragment;
import com.ramin.chat.ui.main.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract ChatListFragment contributeChatListFragment();

    @ContributesAndroidInjector
    abstract ContactsFragment contributeContactsFragment();

    @ContributesAndroidInjector
    abstract SettingsFragment contributeSettingsFragment();

    @ContributesAndroidInjector
    abstract ChatFragment contributeChatFragment();


}

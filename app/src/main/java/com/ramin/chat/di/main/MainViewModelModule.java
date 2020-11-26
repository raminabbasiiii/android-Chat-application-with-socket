package com.ramin.chat.di.main;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.di.ViewModelKey;
import com.ramin.chat.ui.main.chat.ChatViewModel;
import com.ramin.chat.ui.main.chatlist.ChatListViewModel;
import com.ramin.chat.ui.main.contacts.ContactsViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel.class)
    public abstract ViewModel bindContactsViewModel(ContactsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel.class)
    public abstract ViewModel bindChatViewModel(ChatViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ChatListViewModel.class)
    public abstract ViewModel bindChatListViewModel(ChatListViewModel viewModel);

}

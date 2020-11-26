package com.ramin.chat.di.login;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.di.ViewModelKey;
import com.ramin.chat.ui.login.LoginViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class LoginViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    public abstract ViewModel bindLoginViewModel(LoginViewModel viewModel);
}

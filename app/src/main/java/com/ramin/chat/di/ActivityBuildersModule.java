package com.ramin.chat.di;

import com.ramin.chat.di.login.LoginModule;
import com.ramin.chat.di.login.LoginViewModelModule;
import com.ramin.chat.di.main.MainFragmentBuildersModule;
import com.ramin.chat.di.main.MainModule;
import com.ramin.chat.di.main.MainViewModelModule;
import com.ramin.chat.ui.login.LoginActivity;
import com.ramin.chat.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector (
            modules = {
                    MainFragmentBuildersModule.class,
                    MainViewModelModule.class,
                    MainModule.class,
            }
    )
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector (
            modules = {
                    LoginModule.class,
                    LoginViewModelModule.class,
            }
    )
    abstract LoginActivity contributeLoginActivity();

}

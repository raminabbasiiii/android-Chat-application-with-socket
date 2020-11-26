package com.ramin.chat.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.github.nkzawa.socketio.client.Socket;
import com.ramin.chat.R;
import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.ActivityMainBinding;
import com.ramin.chat.ui.login.LoginActivity;
import com.ramin.chat.ui.main.chatlist.ChatListItemOnClick;
import com.ramin.chat.ui.main.contacts.ContactsListItemOnClick;
import com.ramin.chat.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends DaggerAppCompatActivity implements ContactsListItemOnClick, ChatListItemOnClick {

    private ActivityMainBinding binding;
    NavHostFragment navHostFragment;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Preferences preferences;

    @Inject
    Socket socket;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        socket.connect();
        socket.emit("nickname",preferences.getUserMobileNumber());
        configureLayout();
        checkIsLogin();
    }

    private void configureLayout() {
        //Configure Navigation Component
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if (navHostFragment != null) {
            NavigationUI.setupWithNavController(binding.bottomNavigation, navHostFragment.getNavController());
        }
    }

    private void checkIsLogin() {
        if (preferences.getUserId() <= 0) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        final Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

        if (binding.bottomNavigation.getSelectedItemId() == R.id.chatListFragment) {
            if (currentFragment instanceof OnBackPressedListener)
                ((OnBackPressedListener) currentFragment).onBackPressed();
            else {
                super.onBackPressed();
                finish();
            }
        } else if (binding.bottomNavigation.getSelectedItemId() == R.id.contactsFragment) {
            if (currentFragment instanceof OnBackPressedListener)
                ((OnBackPressedListener) currentFragment).onBackPressed();
            else
                binding.bottomNavigation.setSelectedItemId(R.id.chatListFragment);
        } else if (binding.bottomNavigation.getSelectedItemId() == R.id.settingsFragment) {
            binding.bottomNavigation.setSelectedItemId(R.id.chatListFragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        socket.disconnect();
    }

    @Override
    public void onItemClick(String mobileNumber, String name, byte[] image) {
        Bundle send  = new Bundle();
        send.putString("mobileNumber",mobileNumber);
        send.putString("name",name);
        send.putByteArray("image",image);
        Navigation.findNavController(this,R.id.navHostFragment).navigate(R.id.action_contactsFragment_to_chatFragment, send);
        binding.bottomNavigation.setVisibility(View.GONE);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    public void onChatListItemClick(String toMobileNumber, String toName, byte[] toImage) {
        Bundle send  = new Bundle();
        send.putString("mobileNumber",toMobileNumber);
        send.putString("name",toName);
        send.putByteArray("image",toImage);
        Navigation.findNavController(this,R.id.navHostFragment).navigate(R.id.action_chatListFragment_to_chatFragment, send);
        binding.bottomNavigation.setVisibility(View.GONE);
    }
}
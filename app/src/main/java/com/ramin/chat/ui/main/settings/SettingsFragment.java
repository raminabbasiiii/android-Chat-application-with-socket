package com.ramin.chat.ui.main.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ramin.chat.R;
import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.FragmentSettingsBinding;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class SettingsFragment extends DaggerFragment {

    public static final String TAG = SettingsFragment.class.getSimpleName();
    private FragmentSettingsBinding binding;

    @Inject
    Preferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setUserProfile();
    }

    private void setUserProfile() {
        Picasso.get().load(preferences.getImage()).placeholder(R.drawable.profile).fit().into(binding.imgProfile);
        String name = preferences.getName() + " " + preferences.getFamily();
        binding.tvName.setText(name);
        binding.tvMobileNumber.setText(preferences.getUserMobileNumber());
    }
}
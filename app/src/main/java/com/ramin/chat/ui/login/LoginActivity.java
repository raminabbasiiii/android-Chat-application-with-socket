package com.ramin.chat.ui.login;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.ActivityLoginBinding;
import com.ramin.chat.model.ContactModel;
import com.ramin.chat.model.LoginModel;
import com.ramin.chat.ui.main.MainActivity;
import com.ramin.chat.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class LoginActivity extends DaggerAppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this,providerFactory).get(LoginViewModel.class);
        btnLoginOnClick();

    }

    private void btnLoginOnClick() {
        binding.btnLogin.setOnClickListener(v -> {
            if (binding.etMobileNumber.getText().toString().isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
            } else if (!binding.etMobileNumber.getText().toString().matches("(\\+98|0)?9\\d{9}")) {
                Toast.makeText(LoginActivity.this, "Mobile number is wrong", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.observeUserLogin(binding.etMobileNumber.getText().toString())
                        .subscribe(new Observer<LoginModel>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(LoginModel loginModel) {

                                if (loginModel.getResponse().equals("SUCCESS")) {
                                    preferences.setUserSharedPreferences(loginModel.getId(), loginModel.getMobileNumber(),loginModel.getName(),loginModel.getFamily(),loginModel.getImage());
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else if (loginModel.getResponse().equals("FAILED")) {
                                    Toast.makeText(LoginActivity.this, "This mobile number not exist in server", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
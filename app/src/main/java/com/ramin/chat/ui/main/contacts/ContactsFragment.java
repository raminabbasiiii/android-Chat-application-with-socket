package com.ramin.chat.ui.main.contacts;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ramin.chat.data.local.Preferences;
import com.ramin.chat.databinding.FragmentContactsBinding;
import com.ramin.chat.model.ContactModel;
import com.ramin.chat.model.UserModel;
import com.ramin.chat.viewmodel.ViewModelProviderFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ContactsFragment extends DaggerFragment {

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private FragmentContactsBinding binding;
    private ContactsViewModel viewModel;
    private byte[] profileImage;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    ContactsAdapter adapter;

    @Inject
    Preferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this,providerFactory).get(ContactsViewModel.class);
        configureLayout();
        requestAppPermission();
        subscribeObserveContacts();
        deleteContact();
    }

    private void configureLayout() {
        //Configure RecyclerView
        binding.contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.contactsRecyclerView.setAdapter(adapter);

    }

    public void subscribeObserveContacts() {
        viewModel.observeContacts()
                .subscribe(new Observer<List<ContactModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ContactModel> contactModels) {
                        adapter.setContacts(contactModels);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void requestAppPermission() {
        if (getActivity() != null) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.
                        requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                getContacts();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            } else {
                Toast.makeText(getActivity(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getContacts() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            String mobileNumber = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            contactValidation(name,mobileNumber);
                        }
                    }
                    if (pCur != null) {
                        pCur.close();
                    }
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    private void contactValidation(String name, String mobileNumber) {
        viewModel.observeUser(mobileNumber)
                .subscribe(new Observer<UserModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserModel userModel) {
                        //Log.i("api", "onNext: connected to server");
                        if (userModel.getResponse() == null) {
                            Log.i("api", "onNext: find contact");
                            viewModel.observeContactStatus(mobileNumber)
                                    .subscribe(new SingleObserver<ContactModel>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(ContactModel contactModel) {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            if (!mobileNumber.equals(preferences.getUserMobileNumber())) {
                                                if (userModel.getImage().isEmpty()) {
                                                    viewModel.insertContact(name, mobileNumber, null);
                                                    Log.i("api", "onNext: inserted");
                                                } else {
                                                    Picasso.get().load(userModel.getImage()).into(new Target() {
                                                        @Override
                                                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                                            profileImage = getBitmapAsByteArray(bitmap);
                                                            viewModel.insertContact(name, mobileNumber, profileImage);
                                                            Log.i("api", "onNext: inserted");
                                                        }

                                                        @Override
                                                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                                        }

                                                        @Override
                                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    });
                        }
                        Log.i("api", "onNext: request field!");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    
    private void deleteContact() {
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            String mobileNumber = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));
                            viewModel.observeContacts()
                                    .subscribe(new Observer<List<ContactModel>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onNext(List<ContactModel> contactModels) {
                                            for (ContactModel contactModel: contactModels) {
                                                if (!contactModel.getMobileNumber().equals(mobileNumber)) {
                                                    viewModel.deleteContact(contactModel.getId())
                                                            .subscribe(new SingleObserver<Void>() {
                                                                @Override
                                                                public void onSubscribe(Disposable d) {

                                                                }

                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                                                                }

                                                                @Override
                                                                public void onError(Throwable e) {

                                                                }
                                                            });
                                                }
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
                    }
                    if (pCur != null) {
                        pCur.close();
                    }
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    @Override
    public void onResume() {
        super.onResume();
        subscribeObserveContacts();
    }
}
package com.ramin.chat.ui.main.contacts;

import androidx.lifecycle.ViewModel;

import com.ramin.chat.data.local.AppDatabase;
import com.ramin.chat.data.network.main.MainApi;
import com.ramin.chat.model.ContactModel;
import com.ramin.chat.model.UserModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactsViewModel extends ViewModel {

    private static final String TAG = "ContactsViewModel";
    private MainApi api;
    private AppDatabase appDatabase;

    @Inject
    public ContactsViewModel(MainApi api, AppDatabase appDatabase) {
        this.api = api;
        this.appDatabase = appDatabase;
    }

    public Observable<UserModel> observeUser(String mobileNumber) {
        return api
                .getUser(mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void insertContact(String name, String mobileNumber, byte[] image) {
        ContactModel contactModel = new ContactModel(name,mobileNumber,image);
        Observable
                .fromCallable(() -> appDatabase.contactDao().insertContacts(contactModel))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Observable<List<ContactModel>> observeContacts() {
        return appDatabase
                .contactDao()
                .getAllContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ContactModel> observeContactStatus(String mobileNumber) {
        return appDatabase
                .contactDao()
                .contactStatus(mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Void> deleteContact(int id) {
        return appDatabase
                .contactDao()
                .deleteContact(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

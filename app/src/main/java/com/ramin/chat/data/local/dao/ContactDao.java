package com.ramin.chat.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.ramin.chat.model.ContactModel;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertContacts(ContactModel contact);

    @Ignore
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id FROM contacts WHERE mobileNumber = :mobileNumber")
    Single<ContactModel> contactStatus(String mobileNumber);

    @Query("SELECT * FROM contacts")
    Observable<List<ContactModel>> getAllContacts();

    @Query("DELETE FROM contacts WHERE id = :id")
    Single<Void> deleteContact(int id);
}

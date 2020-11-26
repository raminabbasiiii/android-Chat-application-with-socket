package com.ramin.chat.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ramin.chat.data.local.dao.ChatDao;
import com.ramin.chat.data.local.dao.ContactDao;
import com.ramin.chat.model.ChatModel;
import com.ramin.chat.model.ContactModel;

@Database(entities = {ContactModel.class, ChatModel.class}, version = 11, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public abstract ChatDao chatDao();
}

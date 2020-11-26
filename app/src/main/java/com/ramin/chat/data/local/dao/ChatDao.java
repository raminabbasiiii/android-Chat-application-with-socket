package com.ramin.chat.data.local.dao;

import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import com.ramin.chat.model.ChatModel;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertChat(ChatModel chat);

    @Ignore
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT id,message,time,date,direction FROM chats WHERE fromMobileNumber = :fromMobileNumber AND toMobileNumber = :toMobileNumber")
    Observable<List<ChatModel>> getChats(String fromMobileNumber, String toMobileNumber);

    @Ignore
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT chats.id,chats.toName,chats.toMobileNumber,chats.message,chats.time,chats.date,contacts.image AS toImage FROM chats INNER JOIN contacts ON chats.toMobileNumber = contacts.mobileNumber GROUP BY toMobileNumber ORDER BY date DESC,time DESC")
    Observable<List<ChatModel>> getChatList();
}

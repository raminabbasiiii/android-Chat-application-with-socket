package com.ramin.chat.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "chats")
public class ChatModel {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "fromName")
    private String fromName;

    @ColumnInfo(name = "toName")
    private String toName;

    @ColumnInfo(name = "fromMobileNumber")
    private String fromMobileNumber;

    @ColumnInfo(name = "toMobileNumber")
    private String toMobileNumber;

    @ColumnInfo(name = "toImage")
    private byte[] toImage;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "direction")
    private String direction;

    public ChatModel( String fromName, String toName, String fromMobileNumber, String toMobileNumber, String message, String time, String date, String direction) {
        this.fromName = fromName;
        this.toName = toName;
        this.fromMobileNumber = fromMobileNumber;
        this.toMobileNumber = toMobileNumber;
        this.message = message;
        this.time = time;
        this.date = date;
        this.direction = direction;
    }

    public byte[] getToImage() {
        return toImage;
    }

    public void setToImage(byte[] toImage) {
        this.toImage = toImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromMobileNumber() {
        return fromMobileNumber;
    }

    public void setFromMobileNumber(String fromMobileNumber) {
        this.fromMobileNumber = fromMobileNumber;
    }

    public String getToMobileNumber() {
        return toMobileNumber;
    }

    public void setToMobileNumber(String toMobileNumber) {
        this.toMobileNumber = toMobileNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

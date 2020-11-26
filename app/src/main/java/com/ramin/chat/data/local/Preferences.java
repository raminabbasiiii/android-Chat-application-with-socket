package com.ramin.chat.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private final Context context;
    private static final String myPref = "userPreferences";
    private static final String userId = "id";
    private static final String userMobileNumber = "mobileNumber";
    private static final String name = "name";
    private static final String family = "family";
    private static final String image = "image";
    private final SharedPreferences preferences;

    public Preferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(myPref,Context.MODE_PRIVATE);
    }

    public void exitAccount() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setUserSharedPreferences (int uId, String uMobileNumber, String uName, String uFamily, String uImage) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(userId,uId);
        editor.putString(userMobileNumber,uMobileNumber);
        editor.putString(name,uName);
        editor.putString(family,uFamily);
        editor.putString(image,uImage);
        editor.apply();

    }

    public  int getUserId() {
        int uId = 0;
        if (preferences.contains(userId)) {
            uId = preferences.getInt(userId,0);
        }
        return uId;
    }

    public  String getUserMobileNumber() {
        String uMobile = null;
        if (preferences.contains(userMobileNumber)) {
            uMobile = preferences.getString(userMobileNumber,"not exist");
        }
        return uMobile;
    }

    public String getName() {
        String uName = null;
        if (preferences.contains(name)) {
            uName = preferences.getString(name,"not exist");
        }
        return uName;
    }

    public String getFamily() {
        String uFamily = null;
        if (preferences.contains(family)) {
            uFamily = preferences.getString(family,"not exist");
        }
        return uFamily;
    }

    public String getImage() {
        String uImage = null;
        if (preferences.contains(image)) {
            uImage = preferences.getString(image,"not exist");
        }
        return uImage;
    }
}

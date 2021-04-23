package com.lyg.factorysmartsystem.ConfigFile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class authdata {
    //    private static authdata mInstance;
    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context mCtx;

    public static final String SHARED_PREF_NAME = "FactorySmartSystem";
    public static final String LOGIN_STATUS = "LOGIN_STATUS";

    private static final String userID = "userID";
    private static final String userName = "userName";
    private static final String userNIK = "userNIK";
    private static final String userMail = "userMail";
    private static final String token = "token";
    private static final String CreateBy = "CreateBy";
    private static final String CreateDate = "CreateDate";
    private static final String LastModifyBy = "LastModifyBy";
    private static final String LastModifyDate = "LastModifyDate";


    public authdata(Context context) {
        this.mCtx = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setdatauser(String xid_user, String xnama_user, String xnik_user,
                            String xmail_user, String xtoken_user,
                            String xcreateby, String xcreatedate,
                            String xlastmodifyby, String xlastmodifydate)
    {
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(userID, xid_user);
        editor.putString(userName, xnama_user);
        editor.putString(userNIK, xnik_user);
        editor.putString(userMail, xmail_user);
        editor.putString(token, xtoken_user);
        editor.putString(CreateBy, xcreateby);
        editor.putString(CreateDate, xcreatedate);
        editor.putString(LastModifyBy, xlastmodifyby);
        editor.putString(LastModifyDate, xlastmodifydate);
        editor.apply();
    }


    public boolean isLogin() {
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

//    public void logout() {
//        editor.clear();
//        editor.commit();
//
//        Intent login = new Intent(mCtx, LoginActivity.class);
//        mCtx.startActivity(login);
//        ((MainActivity) mCtx).finish();
//    }

    public String getUserID() {
        return sharedPreferences.getString(userID, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(userName, null);
    }

    public String getUserNIK() {
        return sharedPreferences.getString(userNIK, null);
    }

    public String getUserMail() {
        return sharedPreferences.getString(userMail, null);
    }

    public String getToken() {
        return sharedPreferences.getString(token, null);
    }

    public String getCreateBy() {
        return sharedPreferences.getString(CreateBy, null);
    }

    public String getCreateDate() {
        return sharedPreferences.getString(CreateDate, null);
    }

    public String getLastModifyBy() {
        return sharedPreferences.getString(LastModifyBy, null);
    }

    public String getLastModifyDate() {
        return sharedPreferences.getString(LastModifyDate, null);
    }

}

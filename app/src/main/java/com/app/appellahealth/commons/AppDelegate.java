package com.app.appellahealth.commons;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.models.User;
import com.google.gson.reflect.TypeToken;
import com.onesignal.OneSignal;

public class AppDelegate extends MultiDexApplication {

    private static final String TAG = AppDelegate.class.getSimpleName();

    private Prefs mPrefs;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        mPrefs = new Prefs();
        mPrefs.getPrefs(getApplicationContext());

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

    }

    public Prefs getReadablePrefs() {
        return mPrefs;
    }

    public Prefs getEditablePrefs() {
        mPrefs.editPrefs();
        return mPrefs;
    }

    public void setUser(User user){
        getEditablePrefs().putObject(Prefs.KEY_user,user).commitPrefs();
    }

    public User getUser(){
        return ((User)getReadablePrefs().getObject(Prefs.KEY_user,new TypeToken<User>(){}.getType()));
    }

    public String getAccessToken(){
        return BuildConfig.BYPASS_LOGIN?BuildConfig.ACCESS_TOKEN:((User)getReadablePrefs().getObject(Prefs.KEY_user,new TypeToken<User>(){}.getType())).getApiKey();
    }


}

package com.app.appellahealth.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.AppDelegate;
import com.onesignal.OneSignal;


abstract class BaseActivity extends AppCompatActivity {
    protected AppDelegate mApp;
    public ProgressDialog pdLoading;
    private void updateLoader(Context context, boolean shouldShow, String msg) {
        if (pdLoading == null) {
            pdLoading = new ProgressDialog(context);
        }
        if (shouldShow) {
            pdLoading.setMessage(msg);
            pdLoading.setCanceledOnTouchOutside(false);
            pdLoading.show();
        } else if (!shouldShow && pdLoading.isShowing()) {
            pdLoading.dismiss();
        }
    }
    public void updateLoader(Context context, boolean shouldShow) {
        updateLoader(context,shouldShow,context.getString(R.string.loading));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        hideSystemUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        OneSignal.clearOneSignalNotifications();

//        hideSystemUI();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mApp = (AppDelegate) getApplication();
        init();
        setupToolbar();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void hideSystemUI(){
        if (Build.VERSION.SDK_INT < 16)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the status bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        hideSystemUI();

    }

    public abstract void init();
    public abstract void setupToolbar();



}

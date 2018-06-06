package com.app.appellahealth.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.AppDelegate;


public abstract class BaseFragment extends Fragment {
    protected AppDelegate mApp;
    public ProgressDialog pdLoading;
    private void updateLoader(Context context, boolean shouldShow, String msg) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void updateLoader(Context context, boolean shouldShow) {
        updateLoader(context,shouldShow,context.getString(R.string.loading));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApp = (AppDelegate) getActivity().getApplication();
        init();
        setupToolbar();

    }

    public abstract void init();
    public abstract void setupToolbar();

}

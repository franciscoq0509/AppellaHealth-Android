package com.app.appellahealth.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.R;
import com.app.appellahealth.activities.RegistrationActivity;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.FragmentHelpdeskBinding;

/**
 * Created by mehuljoisar on 01/05/18.
 */

public class HelpDeskFragment extends BaseFragment {

    private static final String TAG = HelpDeskFragment.class.getSimpleName();

    private final String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
    private final int RC_CALL_PHONE = 10001;


    private FragmentHelpdeskBinding mBinding;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            callHelpdesk();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_helpdesk,null,false);
        return mBinding.getRoot();
    }


    @Override
    public void init() {
        mBinding.ivNursing.setOnClickListener(mCommonClickListener);
        mBinding.ivFacilities.setOnClickListener(mCommonClickListener);
        mBinding.ivHR.setOnClickListener(mCommonClickListener);
        mBinding.ivIT.setOnClickListener(mCommonClickListener);
    }

    @Override
    public void setupToolbar() {

    }

    public boolean hasCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, RC_CALL_PHONE);
                return false;
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case RC_CALL_PHONE:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callHelpdesk();
                }
                break;
        }
    }

    private void callHelpdesk() {
        if (hasCallPermission()) {
            startActivity(Utils.getStartCallIntent(BuildConfig.CONTACT_HELPDESK));
        }

    }
}

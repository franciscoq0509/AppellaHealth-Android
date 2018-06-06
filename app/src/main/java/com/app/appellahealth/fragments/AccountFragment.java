package com.app.appellahealth.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.app.appellahealth.R;
import com.app.appellahealth.activities.ForgotPasswordActivity;
import com.app.appellahealth.activities.HomeActivity;
import com.app.appellahealth.activities.LoginActivity;
import com.app.appellahealth.activities.PrivacyActivity;
import com.app.appellahealth.commons.Prefs;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.FragmentAccountBinding;
import com.app.appellahealth.models.UserInfo;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.RespAbstract;
import com.app.appellahealth.webservices.resp.RespPrivacyPolicy;
import com.app.appellahealth.webservices.resp.RespUserInfo;
import com.onesignal.OneSignal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mehuljoisar on 01/05/18.
 */

public class AccountFragment extends BaseFragment {

    private static final String TAG = AccountFragment.class.getSimpleName();


    private FragmentAccountBinding mBinding;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvLogout:
                    Utils.logout(getActivity());
                    break;
                case R.id.tvPolicy:
                    Intent intent = new Intent(getActivity(), PrivacyActivity.class);
                    intent.putExtra("url", "http://www.google.com");
                    startActivity(intent);
                    break;
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mCommonCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            updateSubscriptionStateAndUpdateUi(isChecked);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void init() {
        mBinding.tvLogout.setOnClickListener(mCommonClickListener);
        mBinding.tvPolicy.setOnClickListener(mCommonClickListener);
        mBinding.swNotifications.setOnCheckedChangeListener(mCommonCheckedChangeListener);
        getSubscriptionStateAndUpdateUi();
    }

    @Override
    public void setupToolbar() {
    }

    private void getSubscriptionStateAndUpdateUi(){
        boolean isEnabled = OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserSubscriptionSetting();
        updateUi(isEnabled);
    }

    private void updateSubscriptionStateAndUpdateUi(boolean enable){
        OneSignal.setSubscription(enable);
        updateUi(enable);
        updateUserInfo();
    }

    private void updateUi(boolean enable){
        mBinding.swNotifications.setOnCheckedChangeListener(null);
        mBinding.swNotifications.setChecked(enable);
        mBinding.swNotifications.setOnCheckedChangeListener(mCommonCheckedChangeListener);
    }

    private void getUserInfo() {

        if (!Utils.isNetwork(getActivity(), true)) {
            return;
        }
        updateLoader(getActivity(), true);

        RestClient.get(mApp.getAccessToken()).userInfo(
        ).enqueue(new Callback<RespUserInfo>() {
            @Override
            public void onResponse(Call<RespUserInfo> call, Response<RespUserInfo> response) {
                updateLoader(getActivity(), false);
                if (response.code() == 200) {
                    RespUserInfo resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(getActivity().getApplicationContext(), resp.getUserInfo().getMessage());
                    } else {
                        updateUi(resp.getUserInfo());
                    }
                }
                else if(response.code() == 401){
                    Utils.logout(getActivity());
                }
                else {
                    Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespUserInfo> call, Throwable t) {
                updateLoader(getActivity(), false);
                Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
            }
        });
    }

    private void updateUserInfo() {

        if (!Utils.isNetwork(getActivity(), true)) {
            return;
        }
        updateLoader(getActivity(), true);

        RestClient.get(mApp.getAccessToken()).updateUser(
                (mBinding.swNotifications.isChecked())?1:0
        ).enqueue(new Callback<RespAbstract>() {
            @Override
            public void onResponse(Call<RespAbstract> call, Response<RespAbstract> response) {
                updateLoader(getActivity(), false);
                if (response.code() == 200) {
                    RespAbstract resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(getActivity().getApplicationContext(), resp.getPayload().getMessage());
                    }
                }
                else if(response.code() == 401){
                    Utils.logout(getActivity());
                }
                else {
                    Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespAbstract> call, Throwable t) {
                updateLoader(getActivity(), false);
                Utils.showToast(getActivity().getApplicationContext(), getString(R.string.err_api_issue));
            }
        });
    }

    private void updateUi(UserInfo userInfo) {
        mBinding.swNotifications.setOnCheckedChangeListener(null);
        if (userInfo.getNotifications() == 1) {
            mBinding.swNotifications.setChecked(true);
        } else {
            mBinding.swNotifications.setChecked(false);
        }
        mBinding.swNotifications.setOnCheckedChangeListener(mCommonCheckedChangeListener);
    }

}

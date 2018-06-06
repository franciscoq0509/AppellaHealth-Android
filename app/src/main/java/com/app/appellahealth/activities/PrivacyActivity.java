package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityPrivacyBinding;
import com.app.appellahealth.models.PrivacyPolicy;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.RespAbstract;
import com.app.appellahealth.webservices.resp.RespPrivacyPolicy;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivacyActivity extends BaseActivity {

    private static final String TAG = PrivacyActivity.class.getSimpleName();

    private ActivityPrivacyBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(PrivacyActivity.this,R.layout.activity_privacy);
    }


    @Override
    public void init() {
        getPrivacyPolicy();
    }

    @Override
    public void setupToolbar() {
        mBinding.toolbar.setTitle(R.string.title_policy);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    private void getPrivacyPolicy(){

        if(!Utils.isNetwork(PrivacyActivity.this,true)){
            return;
        }
        updateLoader(PrivacyActivity.this, true);

        RestClient.get(mApp.getAccessToken()).privacyPolicy(
        ).enqueue(new Callback<RespPrivacyPolicy>() {
            @Override
            public void onResponse(Call<RespPrivacyPolicy> call, Response<RespPrivacyPolicy> response) {
                updateLoader(PrivacyActivity.this, false);
                if (response.code() == 200) {
                    RespPrivacyPolicy resp = response.body();
                    if (!resp.isSuccesful()) {
                        Utils.showToast(PrivacyActivity.this.getApplicationContext(), resp.getPrivacyPolicy().getMessage());
                    } else {
                        updateUi(resp.getPrivacyPolicy());
                    }
                }
                else if(response.code() == 401){
                    Utils.logout(PrivacyActivity.this);
                }
                else {
                    Utils.showToast(PrivacyActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                }
            }

            @Override
            public void onFailure(Call<RespPrivacyPolicy> call, Throwable t) {
                updateLoader(PrivacyActivity.this, false);
                Utils.showToast(PrivacyActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
            }
        });
    }

    private void updateUi(PrivacyPolicy privacyPolicy){
        Utils.setHTMLData(mBinding.tvPolicy,privacyPolicy.getPrivacyPolicy());
    }

}

package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import com.app.appellahealth.R;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityForgotPasswordBinding;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.RespAbstract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mBinding;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvSubmit:
                    performForgotPassword();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);
    }


    @Override
    public void init() {
        mBinding.tvSubmit.setOnClickListener(mCommonClickListener);
    }

    @Override
    public void setupToolbar() {
        mBinding.toolbar.toolbar.setTitle(R.string.title_forgotPassword);
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void performForgotPassword(){
        if(isValidInputs()){

            if(!Utils.isNetwork(ForgotPasswordActivity.this,true)){
                return;
            }
            updateLoader(ForgotPasswordActivity.this, true);

            RestClient.get(null).forgotPassword(
                    mBinding.etWorkEmail.getText().toString().trim()
            ).enqueue(new Callback<RespAbstract>() {
                @Override
                public void onResponse(Call<RespAbstract> call, Response<RespAbstract> response) {
                    updateLoader(ForgotPasswordActivity.this, false);
                    if (response.code() == 200) {
                        RespAbstract resp = response.body();
                        if (!resp.isSuccesful()) {
                            Utils.showToast(ForgotPasswordActivity.this.getApplicationContext(), resp.getPayload().getMessage());
                        } else {
                            Utils.showToast(ForgotPasswordActivity.this.getApplicationContext(), resp.getPayload().getMessage());
                            navigateToLogin();
                        }
                    } else {
                        Utils.showToast(ForgotPasswordActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                    }
                }

                @Override
                public void onFailure(Call<RespAbstract> call, Throwable t) {
                    updateLoader(ForgotPasswordActivity.this, false);
                    Utils.showToast(ForgotPasswordActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                }
            });
        }
    }

    private boolean isValidInputs(){
        if (TextUtils.isEmpty(mBinding.etWorkEmail.getText().toString().trim()) || (!Patterns.EMAIL_ADDRESS.matcher(mBinding.etWorkEmail.getText().toString().trim()).matches())) {
            Utils.showToast(getApplicationContext(), getString(R.string.err_invalid_field, mBinding.etWorkEmail.getHint().toString()));
            return false;
        }
        return true;
    }

    private void navigateToLogin(){
        startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
        finish();
    }



}

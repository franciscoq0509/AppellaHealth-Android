package com.app.appellahealth.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.R;
import com.app.appellahealth.commons.Prefs;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityRegistrationBinding;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.RespAbstract;
import com.app.appellahealth.webservices.resp.RespLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity {

    private ActivityRegistrationBinding mBinding;

    private final String[] permissions = new String[]{Manifest.permission.CALL_PHONE};
    private final int RC_CALL_PHONE = 10001;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvRegister:
                    performRegistration();
                    break;
                case R.id.tvCallSupport:
                    callForSupport();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
    }


    @Override
    public void init() {
        mBinding.tvRegister.setOnClickListener(mCommonClickListener);
        mBinding.tvCallSupport.setOnClickListener(mCommonClickListener);
        mBinding.tvCallSupport.setText(getString(R.string.hlink_callSupport, BuildConfig.CONTACT_SUPPORT));
    }

    @Override
    public void setupToolbar() {
        mBinding.toolbar.toolbar.setTitle(R.string.title_register);
        setSupportActionBar(mBinding.toolbar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    public boolean hasCallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(RegistrationActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(RegistrationActivity.this, permissions, RC_CALL_PHONE);
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
                    callForSupport();
                }
                break;
        }
    }

    private void callForSupport() {
        if (hasCallPermission()) {
            startActivity(Utils.getStartCallIntent(BuildConfig.CONTACT_SUPPORT));
        }

    }

    private void performRegistration(){
        if(isValidInputs()){

            if(!Utils.isNetwork(RegistrationActivity.this,true)){
                return;
            }
            updateLoader(RegistrationActivity.this, true);

            RestClient.get(null).register(
                    mBinding.etWorkEmail.getText().toString().trim(),
                    mBinding.etPassword.getText().toString().trim(),
                    mBinding.etFirstName.getText().toString().trim(),
                    mBinding.etLastName.getText().toString().trim()
            ).enqueue(new Callback<RespAbstract>() {
                @Override
                public void onResponse(Call<RespAbstract> call, Response<RespAbstract> response) {
                    updateLoader(RegistrationActivity.this, false);
                    if (response.code() == 200) {
                        RespAbstract resp = response.body();
                        if (!resp.isSuccesful()) {
                            Utils.showToast(RegistrationActivity.this.getApplicationContext(), resp.getPayload().getMessage());
                        } else {
                            Utils.showToast(RegistrationActivity.this.getApplicationContext(), resp.getPayload().getMessage());
                            navigateToLogin();
                        }
                    } else {
                        Utils.showToast(RegistrationActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                    }
                }

                @Override
                public void onFailure(Call<RespAbstract> call, Throwable t) {
                    updateLoader(RegistrationActivity.this, false);
                    Utils.showToast(RegistrationActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                }
            });
        }
    }

    private boolean isValidInputs(){
        if (TextUtils.isEmpty(mBinding.etWorkEmail.getText().toString().trim()) || (!Patterns.EMAIL_ADDRESS.matcher(mBinding.etWorkEmail.getText().toString().trim()).matches())) {
            Utils.showToast(getApplicationContext(), getString(R.string.err_invalid_field, mBinding.etWorkEmail.getHint().toString()));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.etPassword.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.err_invalid_field, mBinding.etPassword.getHint().toString()));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.etFirstName.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.err_invalid_field, mBinding.etFirstName.getHint().toString()));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.etLastName.getText().toString().trim())) {
            Utils.showToast(getApplicationContext(), getString(R.string.err_invalid_field, mBinding.etLastName.getHint().toString()));
            return false;
        }

        return true;
    }

    private void navigateToLogin(){
        RestClient.REST_CLIENT = null;
        startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
        finish();
    }





}

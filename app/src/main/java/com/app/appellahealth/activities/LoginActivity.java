package com.app.appellahealth.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.R;
import com.app.appellahealth.commons.Prefs;
import com.app.appellahealth.commons.Utils;
import com.app.appellahealth.databinding.ActivityLoginBinding;
import com.app.appellahealth.models.User;
import com.app.appellahealth.webservices.api.RestClient;
import com.app.appellahealth.webservices.resp.AbstractResp;
import com.app.appellahealth.webservices.resp.RespLogin;
import com.google.gson.reflect.TypeToken;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding mBinding;

    private View.OnClickListener mCommonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvLogin:
                    performLogin();
                    break;
                case R.id.tvNewUser:
                    startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                    break;
                case R.id.tvForgotPassword:
                    startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isLoggedIn()){
            navigateToHome();
        }
        else{
            mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        }
    }

    private boolean isLoggedIn(){
        Prefs prefs = new Prefs(getApplicationContext());
        return prefs.getBoolean(Prefs.KEY_is_logged_in,false);
    }


    @Override
    public void init() {
        mBinding.tvLogin.setOnClickListener(mCommonClickListener);
        mBinding.tvNewUser.setOnClickListener(mCommonClickListener);
        mBinding.tvForgotPassword.setOnClickListener(mCommonClickListener);
    }

    @Override
    public void setupToolbar() {
    }

    private void performLogin(){
        if(isValidInputs()){

            if(!Utils.isNetwork(LoginActivity.this,true)){
                return;
            }
            updateLoader(LoginActivity.this, true);

            RestClient.get(null).login(
                    mBinding.etWorkEmail.getText().toString().trim(),
                    mBinding.etPassword.getText().toString().trim()
            ).enqueue(new Callback<RespLogin>() {
                @Override
                public void onResponse(Call<RespLogin> call, Response<RespLogin> response) {
                    updateLoader(LoginActivity.this, false);
                    if (response.code() == 200) {
                        RespLogin resp = response.body();
                        if (!resp.isSuccesful()) {
                            Utils.showToast(LoginActivity.this.getApplicationContext(), resp.getUser().getMessage());
                        } else {
                            mApp.getEditablePrefs().putBoolean(Prefs.KEY_is_logged_in,true).commitPrefs();
                            mApp.getEditablePrefs().putObject(Prefs.KEY_user,resp.getUser()).commitPrefs();
                            navigateToHome();
                        }
                    } else {
                        Utils.showToast(LoginActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
                    }
                }

                @Override
                public void onFailure(Call<RespLogin> call, Throwable t) {
                    updateLoader(LoginActivity.this, false);
                    Utils.showToast(LoginActivity.this.getApplicationContext(), getString(R.string.err_api_issue));
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
        return true;
    }

    private void navigateToHome(){
        RestClient.REST_CLIENT = null;
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        finish();
    }

}

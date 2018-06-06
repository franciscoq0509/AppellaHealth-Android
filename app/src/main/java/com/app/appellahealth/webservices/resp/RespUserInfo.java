package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.PrivacyPolicy;
import com.app.appellahealth.models.UserInfo;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespUserInfo extends AbstractResp {

    @SerializedName("payload")
    private UserInfo userInfo;

    public RespUserInfo() {
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}

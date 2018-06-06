package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.PrivacyPolicy;
import com.app.appellahealth.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespPrivacyPolicy extends AbstractResp {

    @SerializedName("payload")
    private PrivacyPolicy privacyPolicy;

    public RespPrivacyPolicy() {
    }

    public PrivacyPolicy getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(PrivacyPolicy privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}

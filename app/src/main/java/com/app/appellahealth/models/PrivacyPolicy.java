package com.app.appellahealth.models;

import com.app.appellahealth.webservices.resp.AbstractPayload;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class PrivacyPolicy extends AbstractPayload{

    @SerializedName("privacy_policy")
    private String privacyPolicy;

    public PrivacyPolicy() {
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}

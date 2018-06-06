package com.app.appellahealth.webservices.resp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 01/05/18.
 */

public class AbstractPayload {
    @SerializedName("message")
    private String message;

    public AbstractPayload() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

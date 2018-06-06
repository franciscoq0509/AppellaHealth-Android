package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespLogin extends AbstractResp {

    @SerializedName("payload")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

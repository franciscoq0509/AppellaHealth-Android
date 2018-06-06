package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespAbstract extends AbstractResp {

    @SerializedName("payload")
    private AbstractPayload payload;

    public RespAbstract() {
    }

    public AbstractPayload getPayload() {
        return payload;
    }

    public void setPayload(AbstractPayload payload) {
        this.payload = payload;
    }
}

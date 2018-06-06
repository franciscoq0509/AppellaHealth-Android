package com.app.appellahealth.webservices.resp;

import com.google.gson.annotations.SerializedName;

public class AbstractResp {


    @SerializedName("status")
    private String status;

    public AbstractResp() {
    }

    public boolean isSuccesful() {
        return status.equalsIgnoreCase("ok");
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

package com.app.appellahealth.models;

import com.app.appellahealth.webservices.resp.AbstractPayload;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class UserInfo extends AbstractPayload {


    @SerializedName("active")
    @Expose
    public Integer active;
    @SerializedName("is_admin")
    @Expose
    public Integer isAdmin;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("notifications")
    @Expose
    public Integer notifications;

    public UserInfo() {
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Integer getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Integer isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNotifications() {
        return notifications;
    }

    public void setNotifications(Integer notifications) {
        this.notifications = notifications;
    }
}

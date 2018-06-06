
package com.app.appellahealth.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo
{

    @SerializedName("image")
    @Expose
    public String image;

    public Photo() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

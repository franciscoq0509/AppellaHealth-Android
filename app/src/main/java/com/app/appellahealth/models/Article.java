
package com.app.appellahealth.models;

import java.util.List;

import com.app.appellahealth.webservices.resp.AbstractPayload;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Article extends AbstractPayload{

    public static class Category
    {
        public static final int news = 1;
        public static final int humanResource = 2;
        public static final int informationTechnology = 3;
        public static final int events = 4;
        public static final int dining = 5;
        public static final int nursing = 8;
        public static final int photoGallery = 9;
    }

    public static class Type
    {
        public static String image = "image";
        public static String video = "video";
    }

    public static class ImageType
    {
        public static String half = "half";
        public static String full = "full";
    }

    @SerializedName("news_id")
    @Expose
    public Integer newsId;
    @SerializedName("category_id")
    @Expose
    public Integer categoryId;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("end_date")
    @Expose
    public String endDate;
    @SerializedName("publish_date")
    @Expose
    public String publishDate;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("thumbnail_image")
    @Expose
    public String thumbnailImage;
    @SerializedName("image_type")
    @Expose
    public String imageType;
    @SerializedName("video")
    @Expose
    public String video;
    @SerializedName("photos")
    @Expose
    public List<Photo> photos = null;

    public Article() {
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}

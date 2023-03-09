package com.eulerity.hackathon.imagefinder.entities;

import com.google.protobuf.Timestamp;

/**
 * Image class to represent an image blueprint
 */
public class Image {

    private final String imageId;
    private final String url;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private ImageType imageType;

    public Image(String imageId, String url, Timestamp createdTime, Timestamp updatedTime) {
        this.imageId = imageId;
        this.url = url;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
        this.imageType = ImageType.OTHER;
    }

    public String getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return url;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public enum ImageType {
        LOGO, PORTRAITS, OTHER
    }
}

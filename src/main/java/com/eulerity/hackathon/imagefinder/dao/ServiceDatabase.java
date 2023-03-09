package com.eulerity.hackathon.imagefinder.dao;

import com.eulerity.hackathon.imagefinder.entities.Image;
import com.google.protobuf.util.Timestamps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDatabase.class);
    private static final ServiceDatabase serviceDatabase = new ServiceDatabase();
    private static final RedisCacheStore redisCacheStore = new RedisCacheStore();
    private static final DataRepository dataRepository = new DataRepository();

    private ServiceDatabase() {
    }

    public static ServiceDatabase getServiceDatabase() {
        return serviceDatabase;
    }


    /**
     * Check if current url is visited or not
     *
     * @param url target url
     * @return true if visited, false if not visited
     */
    public boolean containsUrl(String url) {
        return redisCacheStore.containsUrl(url);
    }

    /**
     * store image Url into redis cache
     *
     * @param imageUrl
     * @param imageId
     */
    public void visitImageUrl(String imageUrl, String imageId) {
        redisCacheStore.insertImage(imageUrl, imageId);
    }

    public void visitImageUrl(String imageUrl) {
        redisCacheStore.insertImage(imageUrl);
    }

    /**
     * store other url into redis cache
     *
     * @param url
     */
    public void visitOtherUrl(String url) {
        redisCacheStore.insertOther(url);
    }

    /**
     * create Image in database
     *
     * @param newImage target Image
     * @return image that is created into database
     */
    public Image createImage(Image newImage) {
        Long currentTime = System.currentTimeMillis();
        newImage.setCreatedTime(Timestamps.fromMillis(currentTime));
        newImage.setUpdatedTime(Timestamps.fromMillis(currentTime));
        dataRepository.addImage(newImage);
        return newImage;
    }

    /**
     * get Image entity from database
     *
     * @param imageId id of target image
     * @return image entity
     */
    public Image getImage(String imageId) {
        return dataRepository.getImageById(imageId);

    }

    /**
     * update Image in database
     *
     * @param newImage expected updated image entity
     * @return updated image
     */
    public Image updateImage(Image newImage) {
        Long currentTime = System.currentTimeMillis();
        newImage.setUpdatedTime(Timestamps.fromMillis(currentTime));
        dataRepository.updateImage(newImage);
        return newImage;
    }

    /**
     * delete image in database and clear url cache in redis
     *
     * @param imageId target id of image
     */
    public void deleteImage(String imageId) {
        redisCacheStore.deleteImageByUrl(dataRepository.getImageById(imageId).getImageUrl());
        dataRepository.deleteImageById(imageId);
    }


}

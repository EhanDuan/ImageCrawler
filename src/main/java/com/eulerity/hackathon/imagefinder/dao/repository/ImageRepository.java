package com.eulerity.hackathon.imagefinder.dao.repository;

import com.eulerity.hackathon.imagefinder.entities.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Image repository class implemented by concurrentHashMap
 */
public class ImageRepository {
    private static final Logger logger = LoggerFactory.getLogger(ImageRepository.class);

    private static volatile ConcurrentHashMap<String, Image> mysql;

    public ImageRepository() {
        mysql = new ConcurrentHashMap<>();
    }

    public Image getImage(String imageId) {
        return mysql.get(imageId);
    }

    public Image createImage(Image newImage) {
        mysql.put(newImage.getImageId(), newImage);
        return newImage;
    }

    public Image updateImage(Image newImage) {
        mysql.put(newImage.getImageId(), newImage);
        return newImage;
    }

    public void deleteImage(String imageId) {
        mysql.remove(imageId);
    }

    public List<Image> listImages() {
        return (List<Image>) mysql.values();
    }
}

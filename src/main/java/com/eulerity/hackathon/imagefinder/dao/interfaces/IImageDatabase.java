package com.eulerity.hackathon.imagefinder.dao.interfaces;

import com.eulerity.hackathon.imagefinder.entities.Image;

import java.util.List;

/**
 * Image Database interface
 */
public interface IImageDatabase {

    Image getImageById(String imageId);

    List<Image> listImages();

    Image addImage(Image image);

    void deleteImageById(String imageId);

    Image updateImage(Image image);
}

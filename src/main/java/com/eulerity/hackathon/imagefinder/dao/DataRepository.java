package com.eulerity.hackathon.imagefinder.dao;

import com.eulerity.hackathon.imagefinder.dao.interfaces.IImageDatabase;
import com.eulerity.hackathon.imagefinder.dao.repository.ImageRepository;
import com.eulerity.hackathon.imagefinder.entities.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Serves as a more general database, for example, if in future user repository is needed, it could be a field of below class
 * It only simulate "persistent layer" in normal software architect.
 */
public class DataRepository implements IImageDatabase {
    private static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    private static final ImageRepository imageRepository = new ImageRepository();

    /**
     * retrieve image object by image id
     *
     * @param imageId image id
     * @return image object
     */
    @Override
    public Image getImageById(String imageId) {
        return imageRepository.getImage(imageId);
    }

    /**
     * list all images in the database
     *
     * @return list of image objects
     */
    @Override
    public List<Image> listImages() {
        return imageRepository.listImages();
    }

    /**
     * create image object into database
     *
     * @param image target image object
     * @return saved image object
     */
    @Override
    public Image addImage(Image image) {
        return imageRepository.createImage(image);
    }

    /**
     * delete image by id
     *
     * @param imageId image id
     */
    @Override
    public void deleteImageById(String imageId) {
        imageRepository.deleteImage(imageId);
    }

    /**
     * update image
     *
     * @param image expected updated image object
     * @return updated image object from database
     */
    @Override
    public Image updateImage(Image image) {
        return imageRepository.updateImage(image);
    }

}

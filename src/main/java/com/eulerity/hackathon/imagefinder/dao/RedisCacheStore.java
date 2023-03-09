package com.eulerity.hackathon.imagefinder.dao;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The class serves as the cache layer, using Java JUC ConcurrentHashMap to implement
 */
public class RedisCacheStore {

    private static final String PRESENT = "PRESENT";

    /**
     * redis is mainly used to de-duplicate url and fast query. Key value are
     * (1) if key is imageUrl, the value is image entity id
     * (2) if key is other hyperLinkUrl, the value is special constant
     */
    private static volatile ConcurrentHashMap<String, String> redis;

    public RedisCacheStore() {
        redis = new ConcurrentHashMap<>();
    }

    /**
     * Check if current url is visited or not
     *
     * @param url target url check
     * @return true when first visited, else false
     */
    public boolean containsUrl(String url) {
        return redis.containsKey(url);
    }

    /**
     * Delete image by url
     *
     * @param imageUrl target image url
     * @return true if delete successfully
     */
    public boolean deleteImageByUrl(String imageUrl) {
        redis.remove(imageUrl);
        return true;
    }

    /**
     * Insert image into cache with url as key, PRESENT as value
     *
     * @param imageUrl image url
     */
    public void insertImage(String imageUrl) {
        redis.put(imageUrl, PRESENT);
    }

    /**
     * Insert image into cache
     *
     * @param imageUrl image url
     * @param imageId  image id
     */
    public void insertImage(String imageUrl, String imageId) {
        redis.put(imageUrl, imageId);
    }

    /**
     * insert other url into cache
     *
     * @param url other url
     */
    public void insertOther(String url) {
        redis.put(url, PRESENT);
    }

}

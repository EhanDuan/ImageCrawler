package com.eulerity.hackathon.imagefinder.utils;

/**
 * String utils class
 */
public class StringUtils {
    /**
     * get image id from file name
     *
     * @param fileName file name
     * @return image id
     */
    public static String getImageIdFromFileName(String fileName) {
        int idx = fileName.indexOf('.');
        return fileName.substring(0, idx);
    }
}

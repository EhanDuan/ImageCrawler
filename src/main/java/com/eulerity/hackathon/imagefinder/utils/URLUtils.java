package com.eulerity.hackathon.imagefinder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public class URLUtils {
    private static final Logger logger = LoggerFactory.getLogger(URLUtils.class);

    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");

    /**
     * Check current url is valid or not
     *
     * @param url target url
     * @return true if valid.
     */
    public static boolean validUrl(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            logger.error("uri syntax error");
            return false;
        }
        if (uri.getHost() == null) {
            return false;
        }
        return uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https");
    }

    /**
     * get host or domain of the target website
     *
     * @param inputUrl
     * @return
     */
    public static String getHost(String inputUrl) {
        String host = "";
        try {
            URL url = new URL(inputUrl);
            host = url.getHost();
            return host;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Check if current url match host or not
     *
     * @param host domain url
     * @param url  target url
     * @return true if match
     */
    public static boolean hostMatch(String host, String url) {
        return host.equals(URLUtils.getHost(url));
    }

    /**
     * Check if url is relative path or absolute path
     * Suppose absolute path starts with "http://" or "https://"
     *
     * @param url target url to check
     * @return if it is relative url, return true; else false.
     */
    public static boolean relativeUrl(String url) {
        return !(url.indexOf("http://") == 0 || url.indexOf("https://") == 0);
    }

    /**
     * Generate absolute url from relative url based on base url
     *
     * @param relativeUrl relative url
     * @param baseUrl     base url
     * @return absolute url
     */
    public static String generateAbsoluteUrl(String relativeUrl, String baseUrl) {
        String abURL = "";
        try {
            // get base URI
            URI base = new URI(baseUrl);
            // parse relative url into absolute url based on base URI
            URI abs = base.resolve(relativeUrl);
            // convert into URL Object
            URL absURL = abs.toURL();
            abURL = absURL.toString();
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException : " + e + " abs : " + "relative : " + relativeUrl + "base :" + baseUrl);
        } catch (URISyntaxException e) {
            logger.error("URISyntaxException : " + e);
        }

        return abURL;

    }

    /**
     * Check if current url is image url or not
     *
     * @param url target url
     * @return true if it is image url
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }
}

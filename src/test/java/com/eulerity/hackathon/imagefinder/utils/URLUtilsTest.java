package com.eulerity.hackathon.imagefinder.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class URLUtilsTest {

    /**
     * test case: start with /
     */
    @Test
    public void generateAbsoluteUrl_startWithSlash() {
        String relativeUrl = "/kog/1";
        String baseUrl = "https://voice.hupu.com/nba";
        String expectedUrl = "https://voice.hupu.com/kog/1";
        assertEquals(expectedUrl, URLUtils.generateAbsoluteUrl(relativeUrl, baseUrl));
    }

    /**
     * Test case: start with ./
     */
    @Test
    public void generateAbsoluteUrl_startWithDotSlash() {
        String relativeUrl = "./67331.html";
        String baseUrl = "https://www.zhitongcaijing.com/content/detail/67331.html";
        String expectedUrl = "https://www.zhitongcaijing.com/content/detail/67331.html";
        assertEquals(expectedUrl, URLUtils.generateAbsoluteUrl(relativeUrl, baseUrl));
    }

    /**
     * Test case: start with ../
     */
    @Test
    public void generateAbsoluteUrl_startWithDotDotSlash() {
        String relativeUrl = "../../gppd/sjjj/201707/t20170708_5363690.html";
        String baseUrl = "https://www.cs.com.cn/ssgs/ssb/201707/t20170707_5363166.html";
        String expectedUrl = "https://www.cs.com.cn/ssgs/gppd/sjjj/201707/t20170708_5363690.html";
        assertEquals(expectedUrl, URLUtils.generateAbsoluteUrl(relativeUrl, baseUrl));
    }

    /**
     * Test case : relative url
     */
    @Test
    public void relativeUrl_relative_input() {
        String relativeUrl = "./67331.html";
        assertTrue(URLUtils.relativeUrl(relativeUrl));
    }

    /**
     * Test case : absolute url
     */
    @Test
    public void relativeUrl_absolute_input() {
        String absoluteUrl = "https://www.cs.com.cn/ssgs/ssb/201707/t20170707_5363166.html";
        assertFalse(URLUtils.relativeUrl(absoluteUrl));
    }

    /**
     * test match function
     */
    @Test
    public void hostMatch_test() {
        String targetUrl = "https://i3.hoopchina.com.cn/newsPost/2337-n0wjb8rc-upload-1678171488901-3.png";
        String host = "https://voice.hupu.com/nba";
        assertFalse(targetUrl.matches(host));
    }

}
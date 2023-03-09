package com.eulerity.hackathon.imagefinder.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void getImageIdFromFileName_test() {
        String fileName = "test.jpeg";
        String expected = "test";
        assertEquals(expected, StringUtils.getImageIdFromFileName(fileName));
    }
}
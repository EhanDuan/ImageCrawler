package com.eulerity.hackathon.imagefinder.crawlers;

import com.eulerity.hackathon.imagefinder.constants.ImageSuffix;
import com.eulerity.hackathon.imagefinder.dao.ServiceDatabase;
import com.eulerity.hackathon.imagefinder.entities.Image;
import com.google.protobuf.util.Timestamps;
import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.FieldSetter;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class ImageDownloaderTest {
    ImageDownloader imageDownloader;

    @Mock  ServiceDatabase serviceDatabase;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        try {
            imageDownloader = new ImageDownloader(new LinkedBlockingQueue<String>(), new CountDownLatch(1));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        imageDownloader.serviceDatabase = serviceDatabase;
    }

    private static final String IMAGE_URL = "https://images.pexels.com/photos/545063/pexels-photo-545063.jpeg?auto=compress&format=tiny";

    /**
     * Test downloadImage method in ImageDownloader class
     * Since it is a private method, java reflection is used here, specially "getDeclaredMethod"
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @Test
    public void downloadImage_test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = imageDownloader.getClass().getDeclaredMethod("downloadImage", String.class, String.class);
        method.setAccessible(true);
        String imageId = UUID.randomUUID().toString();
        method.invoke(imageDownloader, imageId, IMAGE_URL);
        // check file exist
        String filePathString = "src/main/resources/downloadImages/" + imageId + ImageSuffix.JPEG;
        File file = new File(filePathString);
        assertTrue(file.exists());

    }
}
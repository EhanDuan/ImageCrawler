package com.eulerity.hackathon.imagefinder.crawlers;

import com.eulerity.hackathon.imagefinder.constants.ImageSuffix;
import com.eulerity.hackathon.imagefinder.dao.ServiceDatabase;
import com.eulerity.hackathon.imagefinder.entities.Image;
import com.eulerity.hackathon.imagefinder.kafka.MessageQueue;
import com.eulerity.hackathon.imagefinder.utils.UserAgentUtils;
import com.google.common.util.concurrent.RateLimiter;
import com.google.protobuf.util.Timestamps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The class serves as consumer of image topic in kafka. ImageDownloader poll urls and perform:
 * 1. create image object and store into db
 * 2. download images files from source url
 */
public class ImageDownloader implements Runnable {
    private static final String DOWNLOAD_PATH = "src/main/resources/downloadImages/";
    private static final Logger logger = LoggerFactory.getLogger(ImageDownloader.class);
    private static final int POOL_SIZE = 4;

    private static final RateLimiter rateLimiter = RateLimiter.create(10);
    private final CountableThreadPool threadPool;
    private final CountDownLatch countDownLatch;
    BlockingQueue<String> imageUrlQueue = MessageQueue.getImageTopicQueue();
    BlockingQueue<String> imageUrlContainer;
    ServiceDatabase serviceDatabase = ServiceDatabase.getServiceDatabase();

    public ImageDownloader(LinkedBlockingQueue<String> imageUrlContainer, CountDownLatch countDownLatch) throws InterruptedException {
        this.imageUrlContainer = imageUrlContainer;
        this.countDownLatch = countDownLatch;
        threadPool = new CountableThreadPool(POOL_SIZE);
    }

    @Override
    public void run() {
        logger.info("ImageDownloader starts work....");
        while (!Thread.currentThread().isInterrupted()) {
            String imageUrl = imageUrlQueue.poll();

            if (imageUrl == null) {
                if (threadPool.getThreadAlive() == 0) {
                    break;
                }
            } else {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rateLimiter.acquire();
                            logger.info("downloader consumes image url: " + imageUrl);
                            imageUrlContainer.put(imageUrl);
                            processImageUrl(imageUrl);
                        } catch (InterruptedException e) {
                            logger.error("InterruptedException when:  image url : " + imageUrl + " error: " + e);
                        }
                    }
                });
            }
        }

        countDownLatch.countDown();
        threadPool.shutdown();
        logger.info("ImageDownloader job done!");
    }

    /**
     * The method process image url with following tasks:
     * 1. create image object and store into db
     * 2. download images files from source url
     *
     * @param imageUrl image url
     */
    private void processImageUrl(String imageUrl) {
        // Step 1. create image object
        String imageId = UUID.randomUUID().toString();
        Long currentTime = System.currentTimeMillis();
        Image newImage = new Image(imageId, imageUrl, Timestamps.fromMillis(currentTime), Timestamps.fromMillis(currentTime));
        serviceDatabase.createImage(newImage);
        // 2. download image
        try {
            downloadImage(imageId, imageUrl);
        } catch (IOException e) {
            logger.error("image downloading IOException: " + e);
        }
    }

    /**
     * Download image from image url and create image object in database
     *
     * @param imageId  image id
     * @param imageUrl target image url
     * @throws IOException
     */
    private void downloadImage(String imageId, String imageUrl) throws IOException {
        File dir = new File(DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String suffix = ImageSuffix.JPG;
        if (imageUrl.contains(ImageSuffix.BMP)) {
            suffix = ImageSuffix.BMP;
        }

        if (imageUrl.contains(ImageSuffix.GIF)) {
            suffix = ImageSuffix.GIF;
        }

        if (imageUrl.contains(ImageSuffix.JPEG)) {
            suffix = ImageSuffix.JPEG;
        }

        if (imageUrl.contains(ImageSuffix.PNG)) {
            suffix = ImageSuffix.PNG;
        }

        File file = new File(DOWNLOAD_PATH + imageId + suffix);
        URL url = new URL(imageUrl);
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("User-Agent", UserAgentUtils.getHeader());
        InputStream inputStream = connection.getInputStream();
        byte[] bs = new byte[1024];
        int len;
        FileOutputStream os = new FileOutputStream(file, true);
        while ((len = inputStream.read(bs)) != -1) {
            os.write(bs, 0, len);
        }

        os.close();
        inputStream.close();
    }

}

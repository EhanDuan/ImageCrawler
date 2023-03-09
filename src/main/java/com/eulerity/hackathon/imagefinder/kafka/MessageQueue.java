package com.eulerity.hackathon.imagefinder.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * class is used as message queue to store urls collected from crawler
 * Producer : crawlers
 * Consumer: (1) Image url: recognizer (2) Other  url: url collectors
 * <p>
 * For image url and other url, there would be 2 message queue to serve specially
 * <p>
 * Future work: Use real kafka topic to differentiate
 */
public class MessageQueue {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueue.class);
    private static final BlockingQueue<String> imageUrlQueue = new LinkedBlockingQueue<>();
    private static final BlockingQueue<String> otherUrlQueue = new LinkedBlockingQueue<>();

    private MessageQueue() {
    }

    public static BlockingQueue<String> getImageTopicQueue() {
        return imageUrlQueue;
    }

    public static BlockingQueue<String> getOtherTopicQueue() {
        return otherUrlQueue;
    }


}

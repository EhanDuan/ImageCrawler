package com.eulerity.hackathon.imagefinder.crawlers;

import com.eulerity.hackathon.imagefinder.dao.ServiceDatabase;
import com.eulerity.hackathon.imagefinder.kafka.MessageQueue;
import com.eulerity.hackathon.imagefinder.utils.URLUtils;
import com.eulerity.hackathon.imagefinder.utils.UserAgentUtils;
import com.google.common.util.concurrent.RateLimiter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 *
 */
public class ThreadCrawler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ThreadCrawler.class);
    private static final int POOL_SIZE = 4;
    private static final BlockingQueue<String> imageUrlQueue = MessageQueue.getImageTopicQueue();
    private static final BlockingQueue<String> otherUrlQueue = MessageQueue.getOtherTopicQueue();
    private static final ServiceDatabase serviceDatabase = ServiceDatabase.getServiceDatabase();
    private static final RateLimiter rateLimiter = RateLimiter.create(10);
    private final CountableThreadPool threadPool;
    private final String host;

    /**
     * The class serves as url crawler to collect urls with a seed url
     *
     * @param url seed url
     * @throws InterruptedException
     */
    public ThreadCrawler(String url) throws InterruptedException {
        this.host = URLUtils.getHost(url);
        this.threadPool = new CountableThreadPool(POOL_SIZE);
        if (URLUtils.isImgUrl(url)) {
            imageUrlQueue.put(url);
            serviceDatabase.visitImageUrl(url);
        } else {
            otherUrlQueue.put(url);
        }
    }

    /**
     * Handle url collection work.
     * push imageUrl into imageUrlQueue, push otherUrl into otherUrlQueue
     */
    @Override
    public void run() {
        logger.info("Start crawling....");
        while (!Thread.currentThread().isInterrupted()) {
            String url = otherUrlQueue.poll();

            if (url == null) {
                if (threadPool.getThreadAlive() == 0) {
                    break;
                }
            } else {
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            rateLimiter.acquire();
                            processUrl(url);
                        } catch (InterruptedException e) {
                            logger.error("process url : " + url + " error: " + e);
                        }
                    }
                });
            }
        }
        threadPool.shutdown();
        logger.info("Crawler url job done");
    }

    /**
     * parse the DOM in the url, check if is image or other href link
     *
     * @param url
     */
    private void processUrl(String url) throws InterruptedException {
        logger.info(Thread.currentThread().getName() + " consumes url : " + url);
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .header("Content-Encoding", "gzip")
                    .userAgent(UserAgentUtils.getHeader())
                    .get();
        } catch (IOException e) {
            logger.error("Jsoup connect IOException: " + e);
        }

        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            String linkHref = link.attr("href");
            visitOtherUrl(linkHref, url);
        }

        Elements imageElements = doc.getElementsByTag("img");

        for (Element element : imageElements) {
            String src = element.attr("src");
            visitImageUrl(src, url);
        }
    }

    /**
     * visit current image url if unvisited
     *
     * @param url  target url
     * @param base base url ("previous url")
     * @throws InterruptedException
     */
    private void visitImageUrl(String url, String base) throws InterruptedException {
        if (URLUtils.relativeUrl(url)) {
            url = URLUtils.generateAbsoluteUrl(url, base);
        }
        if (serviceDatabase.containsUrl(url)) return;
        imageUrlQueue.put(url);
        serviceDatabase.visitImageUrl(url);
        logger.info(Thread.currentThread().getName() + " push image url : " + url);
    }

    private void visitOtherUrl(String url, String base) throws InterruptedException {
        if (URLUtils.relativeUrl(url)) {
            if (url.indexOf(':') > -1) return;
            url = URLUtils.generateAbsoluteUrl(url, base);
        }
        if (serviceDatabase.containsUrl(url)) return;
        if (!URLUtils.hostMatch(host, url)) return;
        serviceDatabase.visitOtherUrl(url);
        otherUrlQueue.put(url);
        logger.info(Thread.currentThread().getName() + " push other url : " + url);
    }
}

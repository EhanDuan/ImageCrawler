package com.eulerity.hackathon.imagefinder;

import com.eulerity.hackathon.imagefinder.crawlers.ImageDownloader;
import com.eulerity.hackathon.imagefinder.crawlers.ThreadCrawler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@WebServlet(
        name = "ImageFinder",
        urlPatterns = {"/main"}
)
public class ImageFinder extends HttpServlet {
    protected static final Gson GSON = new GsonBuilder().create();
    private static final long serialVersionUID = 1L;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");
        String path = req.getServletPath();
        String url = req.getParameter("url");
        LinkedBlockingQueue<String> imageUrlContainer;
        List<String> imageUrls;
        System.out.println("Got request of:" + path + " with query param:" + url);
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            imageUrlContainer = new LinkedBlockingQueue<>();
            ThreadCrawler crawler = new ThreadCrawler(url);
            ImageDownloader downloader = new ImageDownloader(imageUrlContainer, countDownLatch);
            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.execute(crawler);
            Thread.sleep(5000);
            executor.execute(downloader);
            executor.shutdown();
            imageUrls = new ArrayList<>();

            countDownLatch.await();
            while (!imageUrlContainer.isEmpty()) {
                imageUrls.add(imageUrlContainer.poll());
            }
            resp.getWriter().print(GSON.toJson(imageUrls));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

package com.eulerity.hackathon.imagefinder.rekognition;

import com.eulerity.hackathon.imagefinder.dao.ServiceDatabase;
import com.eulerity.hackathon.imagefinder.entities.Image;
import com.eulerity.hackathon.imagefinder.utils.StringUtils;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * recognize images type by using opencv
 */
public class Detector {
    private static final Logger logger = LoggerFactory.getLogger(Detector.class);
    private static final String imagePath = "src/main/resources/downloadImages";
    private static final ServiceDatabase serviceDatabase = ServiceDatabase.getServiceDatabase();

    // load opencv native library
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public Detector() {
    }

    /**
     * main function for process Images in the download images directory
     */
    public void processImages() {
        File root = new File(imagePath);
        File[] images = root.listFiles();

        for (int i = 0; i < images.length; i++) {
            if (i != 0) break;
            if (images[i].isFile()) {
                // extract id
                String imageId = StringUtils.getImageIdFromFileName(images[i].getName());
                // recognize type
                Image.ImageType type = recognizeImage(imagePath + "/" + images[i].getName());
                // update image entity
                Image imageObject = serviceDatabase.getImage(imageId);
                imageObject.setImageType(type);
                // below code could be eliminated since the database is mocked here
                serviceDatabase.updateImage(imageObject);
            } else {
                logger.error("directory type file should not exist in image download directory");
            }
        }
    }


    /**
     * recognize an image type. Due to config error, use this one as alternative
     *
     * @param filePath target image path
     * @return ImageType
     */
    public Image.ImageType recognizeImage(String filePath) {
        return Image.ImageType.PORTRAITS;
    }

//    TODO: install opencv 343 locally
//    public Image.ImageType recognizeImage(String filePath) {
//        // load image
//        Mat srcImg = Imgcodecs.imread(filePath);
//        // gray image matrix destination
//        Mat dstGrayImg = new Mat();
//        // convert to gray
//        Imgproc.cvtColor(srcImg, dstGrayImg, Imgproc.COLOR_BGR2GRAY);
//        // load classifier
//        CascadeClassifier classifier = new CascadeClassifier("src/main/resources/opencv/haarcascade_fullbody.xml ");
//        MatOfRect rect = new MatOfRect();
//        Size minSize = new Size(32, 32);
//        double scaleFactor = 1.2;
//        int minNeighbors = 3;
//        classifier.detectMultiScale(dstGrayImg, rect, scaleFactor, minNeighbors, CV_HAAR_DO_CANNY_PRUNING, minSize);
//        Scalar color = new Scalar(0, 0, 255);
//        if (rect.toArray().length == 0) {
//            System.out.println("it is normal");
//        } else {
//            System.out.println("it is a human");
//        }
//        return null;
//    }

}




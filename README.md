# ImageFinder
The Image Finder is a software application that enables users to efficiently extract images from websites for analysis or use in their projects. Developed as part of the Eulerity Take Home Test, this tool is designed to be reliable and easy to use, catering to the needs of web developers, designers, and content creators.

The whole design doc could be found [here](https://docs.google.com/document/d/1YqDtNqtBxMC7VbF-Nt4MMYnaO71H8LCY2ibUX69fO68/edit?usp=sharing).

## Objective
- crawl website and identify different types of images such as JPEG, PNG, and GIF.
- The tool is able to recognize normal images, logo, and pictures with person.

## Install
Please make sure following environment:
- Maven 3.5 or higher
- Java 8
  - Exact version, **NOT** Java 9+ - the build will fail with a newer version of Java

To start, open a terminal window and navigate to wherever you unzipped to the root directory `imagefinder`. To build the project, run the command:

>`mvn package`

If all goes well you should see some lines that end with "BUILD SUCCESS". When you build your project, maven should build it in the `target` directory. To clear this, you may run the command:

>`mvn clean`

To run the project, use the following command to start the server:

>`mvn clean test package jetty:run`

You should see a line at the bottom that says "Started Jetty Server". Now, if you enter `localhost:8080` into your browser, you should see the `index.html` welcome page! If all has gone well to this point, you're ready to begin!


## Notes

- There are 3 test cases in the `test-links.txt`. The first one might require several minutes to complete. Others could complete in 60s.

- When testing, please also use the terminal to check logs.

- For websites those use pictures url which are not in websites' domain, images are also included. But pages out of domain are excluded.

- When the terminal shows "ImageDownloader job done", downloaded images could be found in `/src/main/resources/downloadImages/`.

- Please refresh your page before starting next crawl job.

- Due to opencv configuration on mac, people recognition feature is not available, although the code is completed.









package com.longhdi.test;

import com.longhdi.entity.User;
import com.longhdi.entity.Video;
import com.longhdi.service.UrlMessageProducer;
import com.longhdi.service.UserService;
import com.longhdi.service.VideoService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(Arquillian.class)
public class VideoIntegrationTest {

    private final String REGISTERED_USER = "REGISTERED_USER";

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addPackages(true,
                        "com.remidemo.service",
                        "com.remidemo.entity", "com.remidemo.component",
                        "com.remidemo.qualifier")
                .addClass(VideoService.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    @Inject
    UserService userService;
    @Inject
    VideoService videoService;

    @Test
    public void shareVideoTest(String userRole) {
        final String VIDEO_TITLE = "Structure and Interpretation of Computer Programs - Chapter 1.2";
        final String PART_OF_DESCRIPTION = "PL Virtual Meetup";
        String email = generateEmail();
        String password = generateRandomStringWithLength(8);
        User user = userService.create(email, password, userRole == null ? REGISTERED_USER : userRole);
        final String URL = "https://youtu.be/b1aAjlNnxT8";
        Optional<Video> video = videoService.create(URL, user);
        Assert.assertTrue(video.isPresent());
        Assert.assertTrue(video.get().getId() != null);
        Assert.assertTrue(video.get().getTitle().equals(VIDEO_TITLE));
        Assert.assertTrue(video.get().getDescription().contains(PART_OF_DESCRIPTION));
    }

    @Inject
    private UrlMessageProducer urlMessageProducer;

    /**
     * IMPORTANT: this test sends 1000 requests to YouTube API.
     * You may run out of YouTube API Quota soon. Please be aware!
     */
    @Test
    public void shareVideoPerformanceTest() {
        String email = "contact@long-hdi.com";
        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty()) userService.create(email, "12345678", "REGISTERED_USER");
        final String URL = "https://youtu.be/b1aAjlNnxT8";
        for (int i = 0; i < 1000; i++) {
            urlMessageProducer.sendMessage(Map.of("email", email, "url", URL));
        }
    }

    private String generateEmail() {
        return String.format("%s@gmail.com", generateRandomStringWithLength(10));
    }

    private String generateRandomStringWithLength(int length) {
        final int a = 97, z = 122;   // characters from 'a' to 'z' are from 97 to 122
        final Random random = new Random();
        final String randomString = random.ints(a, z)
                .mapToObj(number -> String.valueOf((char) number))
                .limit(length).collect(Collectors.joining());
        return randomString;
    }

}

package com.longhdi.test;

import com.longhdi.component.video.VideoFactory;
import com.longhdi.qualifier.YouTube;
import com.longhdi.component.video.YouTubeUrlVideoFactory;
import com.longhdi.entity.Video;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Optional;

@RunWith(Arquillian.class)
public class YouTubeVideoInfoIntegrationTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com.remidemo")
                .addClasses(VideoFactory.class, YouTubeUrlVideoFactory.class, Video.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    @Inject
    @YouTube
    VideoFactory<String> videoFactory;

    final String REAL_ID = "b1aAjlNnxT8";
    final String FAKE_ID = REAL_ID + "_fake";
    final String URL = "https://youtu.be/";
    final String vnExpressUrl = "https://vnexpress.net/";

    @Test
    public void fetchInfoFromARealVideoOnYoutube() {
        Optional<Video> video = videoFactory.buildVideo(URL + REAL_ID);
        Assert.assertTrue(video.isPresent());
        Assert.assertTrue(video.isPresent());
        Assert.assertTrue(video.get().getTitle().equals("Structure and Interpretation of Computer Programs - Chapter 1.2"));
        Assert.assertTrue(video.get().getDescription().contains("PL Virtual Meetup"));
    }

    @Test
    public void fetchInfoFromAVideoWhichDoesntExist() {
        Optional<Video> video2 = videoFactory.buildVideo(URL + FAKE_ID);
        Assert.assertTrue(video2.isEmpty());
    }

    @Test
    public void tryToFetchVideoInfoFromARandomUrl() {
        Optional<Video> video = videoFactory.buildVideo(vnExpressUrl);
        Assert.assertTrue(video.isEmpty());
    }

}

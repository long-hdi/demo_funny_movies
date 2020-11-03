package com.longhdi.test;

import com.longhdi.qualifier.Id;
import com.longhdi.component.MayTransform;
import com.longhdi.qualifier.YouTube;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RunWith(Arquillian.class)
public class VideoIdExtractingTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com.remidemo")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    final String REAL_ID = "b1aAjlNnxT8";
    final String FAKE_ID = REAL_ID + "_fake";

    @Inject @YouTube @Id
    MayTransform<String> extractor;

    @Test
    public void extractYouTubeVideoId() {
        List<Optional<String>> ids = prepareUrlsForTesting(REAL_ID).stream()
                .map(url -> extractor.apply(url))
                .collect(Collectors.toList());
        for (Optional<String> id : ids) {
            Assert.assertTrue(id.isPresent());
            Assert.assertTrue(id.get().equals(REAL_ID));
            Assert.assertFalse(id.get().equals(FAKE_ID));
        }
    }

    private List<String> prepareUrlsForTesting(String id) {
        return Arrays.asList(
                "http://youtu.be/",
                "https://www.youtube.com/watch?v=",
                "http://www.youtube.com/embed/"
        ).stream().map(url -> url + id).collect(Collectors.toList());
    }

}

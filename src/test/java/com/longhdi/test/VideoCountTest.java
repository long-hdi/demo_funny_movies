package com.longhdi.test;

import com.longhdi.entity.VideoCount;
import com.longhdi.service.CountService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(Arquillian.class)
public class VideoCountTest {

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com.remidemo")
                .addClass(CountService.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    @Inject
    private CountService countService;
    @Test
    public void incrementCount() {
        VideoCount currentCount = countService.getCurrentCount();
        VideoCount newCount = countService.incrementTotalVideoCount();
        Assert.assertTrue(newCount.getValue() - currentCount.getValue() == 1);
    }

}

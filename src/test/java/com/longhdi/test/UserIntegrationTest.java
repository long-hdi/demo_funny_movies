package com.longhdi.test;

import com.longhdi.entity.User;
import com.longhdi.security.UserIdentityStore;
import com.longhdi.service.UserService;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Random;
import java.util.stream.Collectors;

@RunWith(Arquillian.class)
public class UserIntegrationTest {

    private final String REGISTERED_USER = "REGISTERED_USER";

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "com.remidemo")
                .addClass(UserService.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        System.out.println(webArchive.toString(true));
        return webArchive;
    }

    @Inject
    UserService userService;

    @Test
    public void signUpTest() {
        String email = generateEmail();
        String password = generateRandomStringWithLength(8);
        User user = userService.create(email, password, REGISTERED_USER);
        Assert.assertTrue(user != null);
        Assert.assertTrue(user.getId() != null);
        Assert.assertTrue(user.getEmail().equals(email));
        Assert.assertTrue(userService.hasValidPassword(password, user));
        Assert.assertFalse(password.equals(user.getPassword()));
        Assert.assertTrue(password.length() < user.getPassword().length());
    }

    @Inject
    UserIdentityStore identityStore;
    @Test
    public void signInTest() {
        String email = generateEmail();
        String password = generateRandomStringWithLength(8);
        userService.create(email, password, REGISTERED_USER);
        UsernamePasswordCredential rightCredential = new UsernamePasswordCredential(email, password);
        CredentialValidationResult validationResult = identityStore.validate(rightCredential);
        Assert.assertTrue(validationResult != CredentialValidationResult.INVALID_RESULT);
        UsernamePasswordCredential wrongPasswordCredential = new UsernamePasswordCredential(email, password + "_");
        CredentialValidationResult wrongPasswordResult = identityStore.validate(wrongPasswordCredential);
        Assert.assertTrue(wrongPasswordResult == CredentialValidationResult.INVALID_RESULT);
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

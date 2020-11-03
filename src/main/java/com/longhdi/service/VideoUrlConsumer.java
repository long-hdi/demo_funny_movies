package com.longhdi.service;

import com.longhdi.entity.User;
import com.longhdi.entity.Video;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = "jms/RemiDemoQueue"),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
public class VideoUrlConsumer implements MessageListener {

    @Inject
    private VideoService videoService;

    @Inject
    private UserService userService;

    @Inject
    private CountService countService;

    @Inject
    private NotificationService notificationService;

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void onMessage(Message message) {
        try {
            String textMessage = ((TextMessage) message).getText();
            String[] messageParts = textMessage.split("\n");
            String email = messageParts[0];
            String url = messageParts[1];
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                Optional<Video> video = videoService.create(url, user.get());
                if (video.isPresent()) {
                    countService.incrementTotalVideoCount();
                    notificationService.createNotification(email, "Your video has been shared!");
                }
            }
        } catch (JMSException e) {
            logger.severe(String.format("JMSException: %s", getStackAsString(e)));
        }
    }

    private String getStackAsString(Throwable ex) {
        return Arrays.stream(ex.getStackTrace()).map(stackTraceElement ->
                "File name: " + stackTraceElement.getFileName() + "\n" +
                        "Class name: " + stackTraceElement.getClassName() + "\n" +
                        "Method name: " + stackTraceElement.getMethodName() + "\n" +
                        "Line number: " + stackTraceElement.getLineNumber()).collect(Collectors.joining("\n" +
                "==========================================================\n")) + ex.getMessage();
    }
}

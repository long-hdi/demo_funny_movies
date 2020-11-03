package com.longhdi.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import java.util.Map;
import java.util.logging.Logger;

@Stateless
public class UrlMessageProducer {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Resource(mappedName = "jms/RemiDemoFactory")
    private ConnectionFactory jmsFactory;
    @Resource(mappedName = "jms/RemiDemoQueue")
    private Queue jmsQueue;

    public void sendMessage(Map<String, String> messageContent) {
        final String email = messageContent.get("email");
        final String url = messageContent.get("url");
        MessageProducer producer;
        TextMessage message;
        try (Connection connection = jmsFactory.createConnection();
             Session session = connection.createSession(false,
                     Session.AUTO_ACKNOWLEDGE)) {
            producer = session.createProducer(jmsQueue);
            message = session.createTextMessage();
            String msg = String.format("%s\n%s", email, url);
            message.setText(msg);
            logger.info(String.format("Message sent to queue: %s", msg));
            producer.send(message);
            producer.close();
        } catch (Exception ex) {
            logger.severe(String.format("Failed to send JMS message: %s", ex.getMessage()));
        }
    }

}

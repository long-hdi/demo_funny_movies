package com.longhdi.service;

import com.longhdi.entity.Notification;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class NotificationService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public List<Notification> getNotificationByEmail(String email) {
        List<Notification> notifications = em.createNamedQuery("Notification.findByUserEmail", Notification.class)
                .setParameter("userEmail", email).getResultList();
        notifications.forEach(notification -> em.remove(notification));
        return notifications;
    }

    public Notification createNotification(String email, String content) {
        Notification notification = new Notification(email, content);
        em.persist(notification);
        return notification;
    }

}

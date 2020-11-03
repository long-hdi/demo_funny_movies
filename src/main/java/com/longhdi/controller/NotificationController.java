package com.longhdi.controller;

import com.longhdi.entity.Notification;
import com.longhdi.service.NotificationService;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestScoped
@Named("notification")
public class NotificationController {

    @Inject
    private NotificationService notificationService;

    @Inject
    private FacesContext facesContext;

    @Inject
    private HttpServletRequest request;

    public void check() {
        List<Notification> notifications = notificationService.getNotificationByEmail(request.getUserPrincipal().getName());
        notifications.forEach(notification -> {
            facesContext.addMessage(null, new FacesMessage(notification.getContent()));
        });
    }

}

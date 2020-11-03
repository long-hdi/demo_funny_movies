package com.longhdi.controller;

import com.longhdi.entity.User;
import com.longhdi.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserController implements Serializable {

    @Inject
    private UserService userService;
    private List<User> userList;

    @PostConstruct
    public void init() {
        userList = userService.findAllUser();
    }

    public List<User> getUserList() {
        return userList;
    }

}

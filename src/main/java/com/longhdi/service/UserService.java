package com.longhdi.service;

import com.longhdi.entity.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserService implements Serializable {

    @PersistenceContext(unitName = "default")
    EntityManager entityManager;

    @Inject
    private Pbkdf2PasswordHash passwordHasher;

    public User create(String email, String password, String role) {
        User newUser = new User(email, passwordHasher.generate(password.toCharArray()), role);
        entityManager.persist(newUser);
        entityManager.flush();
        return newUser;
    }

    public List<User> findAllUser() {
        return entityManager.createNamedQuery("User.findAll", User.class).getResultList();
    }

    public Optional<User> findByEmail(String email) {
        return entityManager.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultStream().findFirst();
    }

    public boolean hasValidPassword(String password, User user) {
        return passwordHasher.verify(password.toCharArray(), user.getPassword());
    }

}

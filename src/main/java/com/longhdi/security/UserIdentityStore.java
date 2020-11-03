package com.longhdi.security;

import com.longhdi.entity.User;
import com.longhdi.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Collections;
import java.util.Optional;

@ApplicationScoped
public class UserIdentityStore implements IdentityStore {

    @Inject
    private UserService userService;

    @Override
    public CredentialValidationResult validate(Credential credential) {
        UsernamePasswordCredential _credential = (UsernamePasswordCredential) credential;
        String email = _credential.getCaller();
        String password = _credential.getPasswordAsString();
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            return userService.hasValidPassword(password, user.get())
                    ? new CredentialValidationResult(email, Collections.singleton(user.get().getGroup()))
                    : CredentialValidationResult.INVALID_RESULT;
        } else {
            User registeredUser = userService.create(email, password, "REGISTERED_USER");
            return new CredentialValidationResult(email, Collections.singleton(registeredUser.getGroup()));
        }
    }
}

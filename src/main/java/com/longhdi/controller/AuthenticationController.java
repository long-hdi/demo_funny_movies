package com.longhdi.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RequestScoped
@Named("authenticator")
public class AuthenticationController {

    @Email(message = "{com.remidemo.validation.Email.message}")
    private String email;

    @Size(min = 8, message = "{com.remidemo.validation.Password.message}")
    private String password;

    @Inject
    private HttpServletRequest request;
    @Inject
    private SecurityContext securityContext;
    @Inject
    private ExternalContext externalContext;
    @Inject
    private FacesContext facesContext;

    public void login() {
        switch (authenticate()) {
            case SEND_CONTINUE:
                facesContext.responseComplete();
                break;
            case SEND_FAILURE:
                FacesMessage message = new FacesMessage(
                        FacesMessage.SEVERITY_ERROR, "Incorrect username or password!", null);
                facesContext.addMessage(null, message);
                //PrimeFaces.current().dialog().showMessageDynamic(message);
                break;
            case SUCCESS:
                facesContext.addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_INFO, "Login succeed", null));
                break;
            case NOT_DONE:
                // Doesn’t happen here
        }
    }

    public String logout() throws ServletException {
        request.logout();
        request.getSession().invalidate();
        return "/index?faces-redirect=true";
    }

    private AuthenticationStatus authenticate() {
        return securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                AuthenticationParameters.withParams().credential(
                        new UsernamePasswordCredential(email, password)));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

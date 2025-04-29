package com.lifedrained.prepjournal.front.pages.login;

import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.front.i18n.RussianLoginForm;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

@PageTitle("Вход")
@Route(Routes.LOGIN_PAGE)
@AnonymousAllowed
@PermitAll

public class LoginPanel extends VerticalLayout  implements BeforeEnterObserver {
    private static final Logger log = LogManager.getLogger(LoginPanel.class);
    private final LoginForm loginForm = new LoginForm();

    private final AuthenticationManager authenticationManager;

    public LoginPanel(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        loginForm.setForgotPasswordButtonVisible(false);
        loginForm.setI18n(new RussianLoginForm());
        loginForm.setAction("login");
        loginForm.addLoginListener(event -> {
            log.info("Login event");;
            auth(event.getUsername(), event.getPassword());
        });
        add(new H1("Введите логин и пароль"), loginForm);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        Map<String, List<String>> params =  beforeEnterEvent.getLocation().getQueryParameters().getParameters();
        if (params.containsKey("error")) {
            loginForm.setError(true);

        }
    }
    private void auth(String login, String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));

        SecurityContextHolder.getContext().setAuthentication(auth);


    }


}

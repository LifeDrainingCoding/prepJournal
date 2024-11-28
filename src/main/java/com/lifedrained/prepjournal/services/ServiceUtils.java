package com.lifedrained.prepjournal.services;

import com.vaadin.flow.component.UI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;


@Service
public class ServiceUtils {
    private static final Logger log = LogManager.getLogger(ServiceUtils.class);
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    public ServiceUtils(HttpServletResponse response, HttpServletRequest request){
        this.request = request;
        this.response = response;

    }
    public void logout(UI ui) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        ui.getUI().ifPresent(ui1 -> {
            ui1.getPage().setLocation("/login");
        });
    }

}

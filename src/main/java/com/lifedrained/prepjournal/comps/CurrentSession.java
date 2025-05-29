package com.lifedrained.prepjournal.comps;

import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.vaadin.flow.server.VaadinSession;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Getter
public class CurrentSession {
    private static final Logger log = LogManager.getLogger(CurrentSession.class);
    private String uid, role;
    private LoginEntity entity;
    public CurrentSession(LoginRepo repo){
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        repo.findByLogin(login).ifPresentOrElse(loginEntity -> {
            log.info("Session opened: "+login);
            entity = loginEntity;
            role = entity.getRole();
            uid = entity.getUid();
        }, () -> log.error("Cannot create session by login: "+login));
        VaadinSession.getCurrent().setAttribute(CurrentSession.class, this);
    }
}

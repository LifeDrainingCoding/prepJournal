package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.Utils.NameProcessor;
import com.lifedrained.prepjournal.repo.SchedulesRepo;
import com.lifedrained.prepjournal.repo.entities.ScheduleEntity;
import com.vaadin.flow.component.UI;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.Lists;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class ServiceUtils implements Comparator<Integer> {
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

        // Используем HTTP-редирект для перенаправления на страницу логина
        ui.getUI().ifPresent(ui1 -> {
            ui1.getPage().setLocation("/login"); // URL для страницы логина
        });
    }


    @Override
    public int compare(Integer startTime, Integer endTime) {
        return 0;
    }
}

package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.comps.CurrentSession;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.services.LoginService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LoginIETab extends AbstractIETab<LoginEntity>{

    private static final Logger log = LogManager.getLogger(LoginIETab.class);
    private LoginService loginService;
    private CurrentSession session;
    @Override
    protected String name() {
        return "Импорт\\Экспорт учетных записей";
    }

    @Override
    protected VerticalLayout importPanel() {
        session = ContextProvider.getBean(CurrentSession.class);
        loginService = ContextProvider.getBean(LoginService.class);
        return new VerticalLayout();
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VerticalLayout();
    }

    @Override
    protected String anchorText() {
        return "Экспортировать учетные записи";
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    protected List<LoginEntity> entitiesToExport() {
        List<LoginEntity> entities = loginService.getRepo().findAll();

        if (session.getRoleConst() != (RoleConsts.ADMIN)){
            entities.removeIf(loginEntity -> {
                RoleConsts loginRole = RoleConsts.valueOf(loginEntity.getRole());
                return loginRole.ordinal()>=session.getRoleConst().ordinal() ;
            });
        }
        return entities ;
    }

    @Override
    @Async
    public void accept(List<List<String>> rows, Throwable throwable) {
        List<LoginEntity> loginsToImport = new ArrayList<>();
        rows.removeFirst();
        log.info("number of rows: {}", rows.size());
        for (int i = 0; i < rows.size(); i++) {
            List<String> row = rows.get(i);
            String name, login, password, role;

            name = row.get(0);
            login = row.get(1);
            password = row.get(2);

            if (containsCyrillic(login) || containsCyrillic(password)) {
                Notify.error("Логин и пароль не должны содержать Кириллицу! Пропуск строки");
                return;
            }


            role = row.get(3);


            String roleConst = containsRole(role);
            if (roleConst == null) {
                Notify.error("Не получилось определить права для " + role + ". Пропуск строки");
                return;
            }

            if (session.getRoleConst() != (RoleConsts.ADMIN)){
                RoleConsts loginRole = RoleConsts.valueOf(roleConst);
                if (loginRole.ordinal()>session.getRoleConst().ordinal()){
                    Notify.error("Ваши права не позволяют добавить "+name+". Пропуск строчки");
                    return;
                }
            }

            LoginEntity loginEntity = new LoginEntity(login, password, roleConst, name);
            loginsToImport.add(loginEntity);
            log.info("imported login entity: {}", loginEntity.getLogin());
        }






        loginService.saveAll(loginsToImport);
        refresh();
        Notify.success("Успешно импортированы учетные записи");
    }
    public boolean containsCyrillic(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '\u0400' && c <= '\u052F') {
                return true;
            }
        }
        return false;
    }

    private String containsRole(String s){
        AtomicReference<String> roleId = new AtomicReference<>("");
        List<RoleConsts> roles = List.of(RoleConsts.values());
        s = s.toLowerCase();
        for (RoleConsts roleConsts : roles) {
            if (roleConsts.translation.toLowerCase().contains(s)) {
                roleId.set(roleConsts.value);
            }
        }
        try {
            log.info("roleId: {}", roleId.get());
             return RoleConsts.valueOf(roleId.get()).name();
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

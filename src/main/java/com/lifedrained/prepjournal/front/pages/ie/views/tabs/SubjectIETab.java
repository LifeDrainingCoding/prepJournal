package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.consts.RoleConsts;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.entities.SubjectEntity;
import com.lifedrained.prepjournal.services.SubjectService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SubjectIETab extends AbstractIETab<SubjectEntity>{

    private static final Logger log = LogManager.getLogger(SubjectIETab.class);
    private SubjectService subjectService;

    public SubjectIETab() {
        super();
    }

    @Override
    protected String name() {
        return "Импорт\\Экспорт дисциплин";
    }

    @Override
    protected VerticalLayout importPanel() {
        subjectService = ContextProvider.getBean(SubjectService.class);
        return new VerticalLayout();
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VerticalLayout();
    }

    @Override
    protected String anchorText() {
        return "Экспортировать дисциплины";
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    protected List<SubjectEntity> entitiesToExport() {
        return subjectService.getRepo().findAll();
    }

    @Override
    public void accept(List<List<String>> rows, Throwable throwable) {
        List<SubjectEntity> subjectsToImport = new ArrayList<>();
        LoginRepo loginRepo = ContextProvider.getBean(LoginRepo.class);
        rows.remove(0);

        rows.forEach(row->{
            String name, masterName, cabinetS;
            name=row.get(0);
            masterName=row.get(1);

            masterName = StringUtils.normalizeSpace(masterName);
            List<String> parts =  List.of(masterName.split(" "));
            LoginEntity loginEntity = null;
            for (String part : parts) {
                log.info(part);
                loginEntity = loginRepo
                        .findByNameContainsIgnoreCaseAndRole(part, RoleConsts.USER_TIER1.value)
                        .orElse(null);
                if (loginEntity != null) {
                    break;
                }
            }
            cabinetS = row.get(2);

            
            if (loginEntity == null) {
                Notify.warning("Преподаватель с именем "+masterName +" не существует! Пропуск строки");
                return;
            }
            cabinetS = cabinetS.trim();
            Pattern cabinetPattern = Pattern.compile("[a-zA-ZА-Яа-я]");
            cabinetS = cabinetPattern.matcher(cabinetS).replaceAll("");

            log.debug("cabinetS: {}", cabinetS);

            while (cabinetS.startsWith("0")){
                cabinetS = cabinetS.replaceFirst("0", "");
            }

            try {
                double cabinetD = Double.parseDouble(cabinetS);
                int cabinet = (int) cabinetD;

                log.debug("cabinet: {}", cabinet);

                SubjectEntity subject = new SubjectEntity(name, loginEntity, cabinet);
                subjectsToImport.add(subject);
            } catch (NumberFormatException e) {
               log.error(e);
               Notify.error("Неправильный формат номера кабинета "+ cabinetS);
            }



        });
        subjectService.saveAll(subjectsToImport);

        Notify.success("Успешно сохранены импортированные данные!");
    }
}

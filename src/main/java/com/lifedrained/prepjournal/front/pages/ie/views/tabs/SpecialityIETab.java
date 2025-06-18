package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.comps.ContextProvider;
import com.lifedrained.prepjournal.repo.SpecialityRepo;
import com.lifedrained.prepjournal.repo.entities.SpecialityEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class  SpecialityIETab extends AbstractIETab<SpecialityEntity>{
    private SpecialityRepo specialityRepo;

    public SpecialityIETab() {
        super();
    }

    @Override
    protected String name() {
        return "Импорт\\Экспорт специальностей";
    }

    @Override
    protected VerticalLayout importPanel() {
        specialityRepo = ContextProvider.getBean(SpecialityRepo.class);
        return new VerticalLayout();
    }

    @Override
    protected VerticalLayout exportPanel() {
        return new VerticalLayout();
    }

    @Override
    protected String anchorText() {
        return "Экспортировать специальности";
    }

    @Override
    protected ComponentEventListener<ClickEvent<Button>> exportClick() {
        return null;
    }

    @Override
    protected List<SpecialityEntity> entitiesToExport() {
        return specialityRepo.findAll();
    }

    @Override
    public void accept(List<List<String>> rows, Throwable throwable) {
        List<SpecialityEntity> specialitiesToImport = new ArrayList<>() ;
        List<String> headerRow = rows.get(0);
        rows.remove(headerRow);

        rows.forEach(row->{

            String name, code;
            name = row.get(0);
            code = row.get(1);

            code = code.trim().replaceAll("[^0-9.]", "");

            if (code.isBlank()){
                Notify.warning("Неверно введен код специальности, строчка будет пропущена ");
                return;
            }


               SpecialityEntity specialityEntity = new SpecialityEntity(name,code);
               specialitiesToImport.add(specialityEntity);


        });
        specialityRepo.saveAll(specialitiesToImport);
        refresh();
        Notify.success("Успешно импортированы специальности");
    }
}

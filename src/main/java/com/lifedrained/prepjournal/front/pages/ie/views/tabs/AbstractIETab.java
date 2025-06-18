package com.lifedrained.prepjournal.front.pages.ie.views.tabs;

import com.lifedrained.prepjournal.Utils.ExcelParser;
import com.lifedrained.prepjournal.Utils.Notify;
import com.lifedrained.prepjournal.front.i18n.CustomUploadI18N;
import com.lifedrained.prepjournal.front.views.Refreshable;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractIETab<T extends BaseEntity> extends Tab
        implements BiConsumer<List<List<String>>, Throwable>, Refreshable {


    @Getter
    protected final VerticalLayout content;
    @Getter
    protected final VerticalLayout importPanel;
    @Getter
    protected final VerticalLayout exportPanel;

    protected Anchor anchor;
    protected CustomButton exportButton;

    protected CustomUploadI18N upload;
    protected MultiFileMemoryBuffer buffer;

    private final String anchorName;
    public AbstractIETab() {
        super();
        setLabel(name());

        anchorName = anchorText();
        buffer = new MultiFileMemoryBuffer();
        upload = new CustomUploadI18N(buffer);
        upload.addSucceededListener(event -> {
            String filename = event.getFileName();
            if (FilenameUtils.getExtension(filename).equalsIgnoreCase("xlsx")) {
                ExcelParser.parseExcel(buffer.getInputStream(filename), this, false);
            }else {
                Notify.error("Формат файла не поддерживается: " + FilenameUtils.getExtension(filename));
            }
        });

        content = new VerticalLayout();
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setWidthFull();

        importPanel = importPanel();
        importPanel.setWidthFull();
        importPanel.add(upload);
        importPanel.addClassName(LumoUtility.Border.BOTTOM);

        exportPanel = exportPanel();
        exportPanel.setWidthFull();
        exportButton = new CustomButton("Экспортировать");

        ComponentEventListener<ClickEvent<Button>> clickListener = exportClick();
         if (clickListener != null) {
             exportButton.addClickListener(clickListener);
         }else{
             exportButton.addClickListener(event -> {
                onExport(entitiesToExport());
             });
         }

         exportPanel.add(exportButton);

         content.add(importPanel, exportPanel);

    }
    protected abstract String name();

    protected abstract VerticalLayout importPanel();

    protected abstract VerticalLayout exportPanel();

    protected abstract String anchorText();

    protected abstract ComponentEventListener<ClickEvent<Button>> exportClick();


    protected abstract List<T> entitiesToExport();



    protected void onExport(List<T> entities) {
        if (anchor != null) {
            exportPanel.remove(anchor);
        }

        anchor = ExcelParser.exportAbstractExcel(entities, anchorName);
        anchor.addClassName(LumoUtility.AlignSelf.CENTER);

        exportPanel.add(anchor);

    }

}

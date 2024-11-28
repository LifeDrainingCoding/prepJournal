package com.lifedrained.prepjournal.front.views;

import com.lifedrained.prepjournal.front.i18n.CustomUploadI18N;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import java.io.InputStream;
import java.io.InputStreamReader;


public class ImportSchedules  extends VerticalLayout  {
    private final CustomUploadI18N upload;
    private final MultiFileMemoryBuffer buffer;
    public ImportSchedules(){
        upload = new CustomUploadI18N();
        buffer = new MultiFileMemoryBuffer();
        upload.setAcceptedFileTypes("text/csv",".csv");
        upload.addSucceededListener(new ComponentEventListener<SucceededEvent>() {
            @Override
            public void onComponentEvent(SucceededEvent event) {
                InputStream is = buffer.getInputStream(event.getFileName());
                InputStreamReader reader = new InputStreamReader(is);
            }
        });
    }

}

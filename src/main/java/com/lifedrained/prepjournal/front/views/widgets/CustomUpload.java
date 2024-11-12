package com.lifedrained.prepjournal.front.views.widgets;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
public class CustomUpload extends Upload {
    public CustomUpload(Receiver receiver){
        UploadI18N uploadI18N = new UploadI18N();
        uploadI18N.setAddFiles(new UploadI18N.AddFiles(){{
           setOne("Загрузить один СSV файл...");
           setMany("Загрузить несколько CSV файлов...");
       }});
        uploadI18N.setDropFiles(new UploadI18N.DropFiles(){{
            setOne("Перекиньте CSV файл сюда");
            setMany("Перекиньте несколько CSV файлов сюда");
        }});
        uploadI18N.setError(new UploadI18N.Error(){{
            setIncorrectFileType("Неправильный формат файла. Формат файла должен быть .csv!");
        }});
        uploadI18N.setUploading(new UploadI18N.Uploading().setStatus(new UploadI18N.Uploading.Status()
                .setConnecting("Соединяемся...").setStalled("Остановлено")
                .setProcessing("Обрабатывается...").setHeld("Обработано")
        ).setRemainingTime(new UploadI18N.Uploading.RemainingTime()
                .setPrefix("Оставшиеся время: ")
                .setUnknown("Неизвестно")));
        setI18n(uploadI18N);
        setReceiver(receiver);
    }
    public CustomUpload(){

    }
}

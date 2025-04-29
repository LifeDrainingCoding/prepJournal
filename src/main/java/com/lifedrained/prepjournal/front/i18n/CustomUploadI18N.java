package com.lifedrained.prepjournal.front.i18n;

import com.vaadin.flow.component.upload.Receiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
public class CustomUploadI18N extends Upload {
    public CustomUploadI18N(Receiver receiver){
        UploadI18N uploadI18N = new UploadI18N();
        uploadI18N.setAddFiles(new UploadI18N.AddFiles(){{
           setOne("Загрузить один СSV или XLSX файл...");
           setMany("Загрузить один СSV или XLSX файл...");
       }});
        uploadI18N.setDropFiles(new UploadI18N.DropFiles(){{
            setOne("Перекиньте CSV или XLSX файл сюда");
            setMany("Перекиньте CSV или XLSX файл сюда");
        }});
        setAcceptedFileTypes("text/csv","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ,".xlsx", ".csv");
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
    public CustomUploadI18N(){

    }
}

package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;

import java.util.function.Consumer;

public class ActionDialog extends Dialog {
    protected CustomButton ok,deny;
    private Consumer<Boolean> callback;
    public ActionDialog( Consumer<Boolean> callback) {
        add(new Span("ВНИМАНИЕ! К выбранным элементам привязаны другие данные. " +
                "\n Удаление выбранных элементов приведет за собой удаление всех связанных данных. Подтвердить?"));
        this.callback = callback;
        initBtns();
    }

    private void initBtns(){
        ok = new CustomButton("Подтвердить");
        ok.addClickListener(event->{
            callback.accept(true);
            close();
        });

        deny = new CustomButton("Отмена");
            deny.addClickListener(event-> {
                callback.accept(false);
                close();
            });



        deny.setTheme(ButtonVariant.LUMO_ERROR);
        getFooter().add(deny,ok);
    }
}

package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.front.interfaces.OnCloseDialogListener;
import com.lifedrained.prepjournal.front.views.Refreshable;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.lifedrained.prepjournal.repo.entities.BaseEntity;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.transaction.Transactional;

public abstract class AbstractDialog<T extends BaseEntity> extends Dialog implements Refreshable {
    protected OnConfirmListener<T> confirmListener;
    protected OnCloseDialogListener<?> closeListener;
    protected VerticalLayout headerLayout;
    protected CustomButton ok,deny;

    protected T entity;

    public AbstractDialog(OnConfirmListener<T> confirmListener, T entity ) {
        this.confirmListener = confirmListener;
        this.entity = entity;
        headerLayout = new VerticalLayout();
        add(headerLayout);
        initBtns();
        setDraggable(true);
    }



    protected  ComponentEventListener<ClickEvent<Button>> onDeny(){
        return null;
    }
    @Transactional
    protected  abstract ComponentEventListener<ClickEvent<Button>> onOk();

    public T getResult(){
        return entity;
    }
    private void initBtns(){
        ok = new CustomButton("Сохранить");
        ok.addClickListener(onOk());

        deny = new CustomButton("Отмена");
        if (closeListener != null){
            deny.addClickListener(onDeny());
        }else {
            deny.addClickListener( event -> close());
        }

        deny.setTheme(ButtonVariant.LUMO_ERROR);
        getFooter().add(deny,ok);
    }

}

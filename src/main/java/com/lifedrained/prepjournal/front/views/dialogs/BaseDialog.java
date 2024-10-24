package com.lifedrained.prepjournal.front.views.dialogs;

import com.lifedrained.prepjournal.front.interfaces.CheckableFields;
import com.lifedrained.prepjournal.front.interfaces.OnCloseDialogListener;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.views.widgets.CustomButton;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;

/**
 *
 * @param <T> - Возвращаемый тип данных в слушателе {@link OnConfirmDialogListener}
 *           , а также тип данных возвращаемый {@link #getDataFromFields()}
 */

public abstract class BaseDialog<T> extends Dialog implements CheckableFields<T> {
    protected OnConfirmDialogListener<T> confirmListener;
    protected OnCloseDialogListener<?> closeListener;
    protected CustomButton ok,deny;
    protected List<T> fieldNames;

    public BaseDialog(OnConfirmDialogListener<T> confirmListener, List<T> fieldNames){
        this.confirmListener =  confirmListener;
        this.fieldNames = fieldNames;
        initBtns();
        setButtonListeners();
    }
    public void setOnCloseListener(OnCloseDialogListener<?> closeListener){
        this.closeListener = closeListener;
    }
    public boolean isCloseListenerNull(){
        return closeListener == null;
    }
    public abstract List<T> getDataFromFields();
    protected abstract void setButtonListeners();
    private void initBtns(){
        ok = new CustomButton("Сохранить");
        deny = new CustomButton("Отмена");
        deny.setTheme(ButtonVariant.LUMO_ERROR);
        getFooter().add(deny,ok);
    }
}

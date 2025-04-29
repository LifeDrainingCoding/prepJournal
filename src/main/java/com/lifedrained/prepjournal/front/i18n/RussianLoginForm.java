package com.lifedrained.prepjournal.front.i18n;

import com.vaadin.flow.component.login.LoginI18n;

public class RussianLoginForm extends LoginI18n {
    public RussianLoginForm() {
        super();

        LoginI18n.Form form = new LoginI18n.Form();

        form.setSubmit("Войти");
        form.setPassword("Пароль");
        form.setTitle("Для продолжения вам нужно войти");
        form.setUsername("Логин");

        setForm(form);

        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setTitle("Ошибка возникла при попытке входа");
        errorMessage.setMessage("Проверьте корректность введенных данных");
        setErrorMessage(errorMessage);

    }
}

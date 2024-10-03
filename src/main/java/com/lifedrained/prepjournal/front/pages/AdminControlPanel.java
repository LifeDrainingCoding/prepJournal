package com.lifedrained.prepjournal.front.pages;

import com.lifedrained.prepjournal.front.interfaces.OnCheckBoxPickedListener;
import com.lifedrained.prepjournal.front.interfaces.OnConfirmDialogListener;
import com.lifedrained.prepjournal.front.interfaces.UserControl;
import com.lifedrained.prepjournal.front.views.ChangeUserDialog;
import com.lifedrained.prepjournal.front.views.TabSheetView;
import com.lifedrained.prepjournal.front.views.UsersControlButtons;
import com.lifedrained.prepjournal.repo.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.time.Duration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.Consumer;


@Route("/admin")
public class AdminControlPanel extends VerticalLayout implements ComponentEventListener<TabSheetView.SelectedChangeEvent>,
        OnCheckBoxPickedListener, UserControl {
    private LoginRepo loginRepo;
    private LinkedHashMap<String, LoginEntity> selectedLogins;
    private UsersControlButtons bar;

   public AdminControlPanel(LoginRepo loginRepo) {
       this.loginRepo = loginRepo;
       selectedLogins = new LinkedHashMap<>();
       bar = new UsersControlButtons(this);
       TabSheetView view = new TabSheetView(loginRepo, this, this);
       setAlignItems(Alignment.CENTER);
       add(view);
   }


    @Override
    public void onComponentEvent(TabSheetView.SelectedChangeEvent event) {
       event.getSelectedTab().getId().ifPresentOrElse(new Consumer<String>() {
           @Override
           public void accept(String string) {
               if(string.equals("usersContent")){
                   addComponentAsFirst(bar);
               }else {
                   remove(bar);
               }
           }
       }, new Runnable() {
           @Override
           public void run() {
               Notification.show("Tab has no id");
           }
       });

    }

    @Override
    public void onChecked(String id, LoginEntity entity, boolean isChecked) {
       if(isChecked){
           selectedLogins.put(id,entity);
       }else {
           selectedLogins.remove(id);
       }
       bar.checkButtons(selectedLogins);
    }


    @Override
    public void onDelete() {

       if(selectedLogins.isEmpty()){
           Notification notification = new Notification("Нельзя удалить ничего. " +
                   "\n Выберите пользователей для удаления.",
                   (int) Duration.ofSeconds(10).toMillis(),
                   Notification.Position.TOP_CENTER);
           notification.open();
           return;
       }

       loginRepo.deleteAll(selectedLogins.values());
        UI.getCurrent().refreshCurrentRoute(true);


    }

    @Override
    public void onUpdate() {
       Iterator<LoginEntity> iterator =selectedLogins.values().iterator();
       while (iterator.hasNext()){
          LoginEntity entity = iterator.next();
           ChangeUserDialog dialog = new ChangeUserDialog(new OnConfirmDialogListener() {
               @Override
               public void onConfirm(String[] data) {
                   entity.setLogin(data[0]);
                   entity.setPassword(data[1]);
                   entity.setRole(data[2]);
                   entity.setName(data[3]);
                   loginRepo.save(entity);
               }
           }, new String[]{entity.getName(), entity.getLogin(),entity.getPassword(), entity.getRole()});
           dialog.open();

       }
    }

    @Override
    public void onCreate() {
        ChangeUserDialog dialog = new ChangeUserDialog(new OnConfirmDialogListener() {
            @Override
            public void onConfirm(String[] data) {
                LoginEntity entity = new LoginEntity();
                entity.setLogin(data[0]);
                entity.setPassword(data[1]);
                entity.setRole(data[2]);
                entity.setName(data[3]);
                loginRepo.save(entity);
                UI.getCurrent().refreshCurrentRoute(true);
            }
        }, new String[]{"Имя: ", "Логин: ","Пароль: ", "Права: "});

        dialog.open();
    }
}

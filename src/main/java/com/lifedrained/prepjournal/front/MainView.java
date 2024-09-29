package com.lifedrained.prepjournal.front;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import com.vaadin.flow.router.Route;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Route("/")
public class MainView extends VerticalLayout  {
    private static final Logger log = LogManager.getLogger(MainView.class);
    private TextField login, password;
    private Button loginBtn;
    public MainView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
       login = new TextField();
       login.setTitle("Login");
       password = new TextField();
       password.setTitle("Password");
       loginBtn = new Button();
       loginBtn.setText("Log in");
       add(new H1("Login form"),login,password,loginBtn );
    }
}

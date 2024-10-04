package com.lifedrained.prepjournal;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.services.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PreConfigureRunner implements CommandLineRunner {
    private LoginService service;


    @Override
    public void run(String... args) throws Exception {
        checkRootAccount();
    }
    private  void checkRootAccount(){
        Optional<LoginEntity> entityOpt = service.findByLogin("root");

        if(entityOpt.isPresent()){
                System.out.println("Root account is present");

        }else {
            System.out.println("no root found, creating a new one");
            LoginEntity root =  new LoginEntity("root","admin137dark3","ADMIN","Корень");
            service.updateUser(root);
        }
    }
}

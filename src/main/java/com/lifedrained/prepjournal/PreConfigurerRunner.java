package com.lifedrained.prepjournal;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.services.LoginService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class PreConfigurerRunner implements CommandLineRunner {
    private LoginService service;


    @Override
    public void run(String... args) throws Exception {
        checkRootAccount();
    }
    private  void checkRootAccount(){
        Optional<LoginEntity> entityOpt = service.findByLogin("root");

        if(entityOpt.isPresent() && entityOpt.get().getPassword().equals("admin137dark3")){
                System.out.println("Root account is present");
        }else {
            entityOpt.ifPresent(loginEntity -> service.getRepo().delete(loginEntity));
            System.out.println("no root found, creating a new one");
            LoginEntity root =  new LoginEntity("root","admin137dark3","ADMIN","Корень");
            service.updateUser(root);
        }
    }
}

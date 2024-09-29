package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.configs.LoginDetails;
import com.lifedrained.prepjournal.repo.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class LoginService implements UserDetailsService {
    LoginRepo repo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<LoginEntity> entity =  repo.findByLogin(login);

        try {
            return entity.map(new Function<LoginEntity, LoginDetails>() {
                @Override
                public LoginDetails apply(LoginEntity loginEntity) {
                    return new LoginDetails(loginEntity);
                }

                @Override
                public <V> Function<V, LoginDetails> compose(Function<? super V, ? extends LoginEntity> before) {
                    return Function.super.compose(before);
                }

                @Override
                public <V> Function<LoginEntity, V> andThen(Function<? super LoginDetails, ? extends V> after) {
                    return Function.super.andThen(after);
                }
            }).orElseThrow(new Supplier<Throwable>() {
                @Override
                public Throwable get() {
                    return new UsernameNotFoundException(login+" does not exist in repo");
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public void updateUser( LoginEntity entity){

        repo.save(entity);
    }
    public Optional<LoginEntity> findById(long id){
        return repo.findById(id);
    }
    public Optional<LoginEntity> findByLogin(String login){
        return repo.findByLogin(login);
    }
}

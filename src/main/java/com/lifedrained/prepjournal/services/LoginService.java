package com.lifedrained.prepjournal.services;

import com.lifedrained.prepjournal.configs.LoginDetails;
import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import com.lifedrained.prepjournal.repo.LoginRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
@Getter
public class LoginService implements UserDetailsService {
     private LoginRepo repo;

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
                    return new UsernameNotFoundException("Пользователь с логином не найден: " + login );
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
    public void updateUser( LoginEntity entity){

        repo.save(entity);
    }
    @Transactional
    public void saveAll(Iterable<LoginEntity> entities){
        repo.saveAll(entities);
    }
    public Optional<LoginEntity> findById(long id){
        return repo.findByIdOrderById(id);
    }
    public Optional<LoginEntity> findByLogin(String login){
        return repo.findByLogin(login);
    }
}

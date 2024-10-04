package com.lifedrained.prepjournal.configs;

import com.lifedrained.prepjournal.repo.entities.LoginEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LoginDetails implements UserDetails {
    private LoginEntity login;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(login.getRole().split(",")).map(new Function<String, GrantedAuthority>() {
            @Override
            public GrantedAuthority apply(String s) {
                return new SimpleGrantedAuthority(s);
            }

            @Override
            public <V> Function<V, GrantedAuthority> compose(Function<? super V, ? extends String> before) {
                return Function.super.compose(before);
            }

            @Override
            public <V> Function<String, V> andThen(Function<? super GrantedAuthority, ? extends V> after) {
                return Function.super.andThen(after);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return login.getPassword();
    }

    @Override
    public String getUsername() {
        return login.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}

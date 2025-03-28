package com.lifedrained.prepjournal.configs;

import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.services.LoginService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static com.lifedrained.prepjournal.consts.RoleConsts.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityCfg implements AuthenticationSuccessHandler {

    LoginService loginService;
    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(loginService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(new Customizer<CsrfConfigurer<HttpSecurity>>() {
            @Override
            public void customize(CsrfConfigurer<HttpSecurity> httpSecurityCsrfConfigurer) {
                httpSecurityCsrfConfigurer.disable();
            }
        }).authorizeHttpRequests(new Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>() {
            @Override
            public void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>
                                          .AuthorizationManagerRequestMatcherRegistry
                                          registry) {
                registry.requestMatchers("/welcome").permitAll()
                        .requestMatchers(Routes.ADMIN_PAGE).hasAuthority("ADMIN")
                        .requestMatchers("/user").hasAnyAuthority("USER","ADMIN")
                        .anyRequest().authenticated();

            }
        }).formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
            @Override
            public void customize(FormLoginConfigurer<HttpSecurity> formCfg) {
                formCfg.successHandler(SecurityCfg.this);
            }
        }).build();
    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
       String role =  authentication.getAuthorities().stream().map(new Function<GrantedAuthority, String>() {

           @Override
           public String apply(GrantedAuthority grantedAuthority) {
               return grantedAuthority.getAuthority();
           }
       }).findFirst().orElse("");
       if (role.equals(ADMIN.value)||role.equals(ADMIN1.value)){
           response.sendRedirect(Routes.ADMIN_PAGE);
       } else if (role.equals(USER.value)||role.equals(USER1.value)) {
           response.sendRedirect(Routes.MASTER_PAGE);
       }else {
           response.sendError(404, "No suitable page found for role: "+role);
       }

    }
}

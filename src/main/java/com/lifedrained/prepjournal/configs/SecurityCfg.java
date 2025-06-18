package com.lifedrained.prepjournal.configs;

import com.lifedrained.prepjournal.consts.Routes;
import com.lifedrained.prepjournal.services.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.io.IOException;
import java.util.List;


import static com.lifedrained.prepjournal.consts.RoleConsts.*;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityCfg implements AuthenticationSuccessHandler  {

    private static final Logger log = LogManager.getLogger(SecurityCfg.class);
    private LoginService loginService;
    @Bean
    public AuthenticationProvider provider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(loginService);
        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        return provider;
    }





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable).authorizeHttpRequests(authRegistry-> {
            authRegistry
                    .requestMatchers("/login.html").permitAll()
                    .requestMatchers(Routes.ADMIN_PAGE).hasAnyAuthority(ADMIN.value,
                            USER_TIER2.value, USER_TIER3.value)
                    .requestMatchers(Routes.NOMENCLATURES_PAGE).hasAnyAuthority(ADMIN.value,
                            USER_TIER2.value, USER_TIER3.value)
                    .requestMatchers(Routes.IE_PAGE).hasAnyAuthority(ADMIN.value,
                            USER_TIER2.value, USER_TIER3.value)
                    .requestMatchers(Routes.SCHEDULE_DETAILS, Routes.MASTER_PAGE, Routes.STATISTICS_PAGE)
                    .hasAnyAuthority(ADMIN.value, USER_TIER1.value)
            .anyRequest().authenticated();
        }).formLogin(formCfg ->{
            formCfg.loginPage("/login.html");
            formCfg.loginProcessingUrl("/login");
            formCfg.successHandler(this);
        });
       return http.build();
    }








    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("onAuthenticationSuccess");
       String role =  authentication.getAuthorities().stream().map(
               grantedAuthority -> grantedAuthority.getAuthority())
               .findFirst().orElse("");
       if (role.equals(ADMIN.value) || role.equals(USER_TIER2.value) || role.equals(USER_TIER3.value)) {
           response.sendRedirect(Routes.ADMIN_PAGE);
       } else if (role.equals(USER_TIER1.value)) {
           response.sendRedirect(Routes.MASTER_PAGE);
       }else {
           response.sendError(404, "No suitable page found for role: "+role);
       }

    }


    @Bean
    public AuthenticationManager authenticationManager(List<AuthenticationProvider> providers) {
        log.info("auth providers size: {}", providers.size());
        return new ProviderManager(providers);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return this;
    }
}

package com.lifedrained.prepjournal;

import jakarta.annotation.security.PermitAll;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class LoginController {
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome page";
    }

    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String users() {
        return "users page";
    }
}

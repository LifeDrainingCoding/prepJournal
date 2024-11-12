package com.lifedrained.prepjournal;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;


@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    @GetMapping("/welcome")
    public String welcome(){
        return "Welcome page";
    }
    @GetMapping("/users")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String users(){
        return "users page";
    }
}

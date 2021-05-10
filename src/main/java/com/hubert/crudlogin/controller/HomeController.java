package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Customer;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String index(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "index";
  }

  @GetMapping("/home")
  public String home() {
    return "home";
  }

  @GetMapping("/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "login";
  }

}

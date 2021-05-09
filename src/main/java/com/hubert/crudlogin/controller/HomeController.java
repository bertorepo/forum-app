package com.hubert.crudlogin.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String index(Authentication authentication) {
    return authentication != null ? "redirect:/authenticated" : "index";
  }

  @GetMapping("/admin")
  public String admin(){
    return "admin";
  }

  @GetMapping("/authenticated")
  public String home() {
    return "authenticated";
  }

  @GetMapping("/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/authenticated" : "login";
  }
  //get the authenticated User
  // private Customer getPrincipal() {
  //   Customer customer = null;

  //   if (
  //     SecurityContextHolder
  //       .getContext()
  //       .getAuthentication()
  //       .getPrincipal() instanceof Customer
  //   ) {
  //     customer =
  //       (Customer) SecurityContextHolder
  //         .getContext()
  //         .getAuthentication()
  //         .getPrincipal();
  //   }
  //   return customer;
  // }
}

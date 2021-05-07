package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Customer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("customer", getPrincipal());
    if (getPrincipal() != null) {
      return "authenticated";
    }
    return "login";
  }

  @GetMapping("/authenticated")
  public String home(Model model) {
    model.addAttribute("customer", getPrincipal());
    return "authenticated";
  }

  @GetMapping("/login")
  public String login() {
    Customer customer = getPrincipal();
    if (customer != null) {
      return "authenticated";
    }

    return "login";
  }

  //get the authenticated User
  private Customer getPrincipal() {
    Customer customer = null;

    if (
      SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal() instanceof Customer
    ) {
      customer =
        (Customer) SecurityContextHolder
          .getContext()
          .getAuthentication()
          .getPrincipal();
    }
    return customer;
  }
}

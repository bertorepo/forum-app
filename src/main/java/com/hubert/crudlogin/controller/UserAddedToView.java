package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Customer;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserAddedToView {

  @ModelAttribute("customer")
  public Customer addToUserView(Authentication authentication) {
    return authentication != null
      ? (Customer) authentication.getPrincipal()
      : null;
  }
}

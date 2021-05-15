package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.objects.CustomerDTO;
import com.hubert.crudlogin.service.CustomerService;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

  private final CustomerService customerService;
  private final ModelMapper modelMapper;
  private static final Logger log = LoggerFactory.getLogger(
    CustomerController.class
  );

  @Autowired
  public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
    this.customerService = customerService;
    this.modelMapper =  modelMapper;
  }

  @InitBinder
  public void initialBinder(WebDataBinder binder) {
    StringTrimmerEditor trimmerEditor = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, trimmerEditor);
  }

  @GetMapping("/register")
  public String showRegisterPage(
    Authentication authentication,
    @ModelAttribute CustomerDTO customerDTO,
    Model model
  ) {
    model.addAttribute("customerDTO", customerDTO);
    return authentication != null ? "redirect:/home" : "register";
  }

  @PostMapping("/register")
  public String save(
    @Valid CustomerDTO customerDTO,
    BindingResult bindingResult,
    RedirectAttributes redirectAttributes,
    Authentication authentication
  ) {
    //check if email exist
    if (customerService.customerExists(customerDTO.getEmail())) {
      bindingResult.addError(
        new FieldError(
          "customerDTO",
          "email",
          "Email already exists in the database"
        )
      );
    }

    //check if username exist
    if (
      customerService
        .findCustomerByUsername(customerDTO.getUsername())
        .isPresent()
    ) {
      bindingResult.addError(
        new FieldError(
          "customerDTO",
          "username",
          "Username already exists in the database"
        )
      );
    }

    //check if password id not null and matches the re enter ppassword field

    if (
      customerDTO.getPassword() != null && customerDTO.getrPassword() != null
    ) {
      if (!customerDTO.getPassword().equals(customerDTO.getrPassword())) {
        bindingResult.addError(
          new FieldError("customerDTO", "rPassword", "Password did not match!")
        );
      }
    }

    //check if fields have any errors
    if (bindingResult.hasErrors()) {
      return "register";
    }

    log.info(">> CustomerDTO " + customerDTO.toString());
    redirectAttributes.addFlashAttribute(
      "success_register",
      "Successfull! You can now login."
    );
    customerService.register(customerDTO);
    return "redirect:/login";
  }

  //profile area

  @GetMapping("/profile")
  public String showProfile(Model model, Authentication authentication, CustomerDTO customerDTO){

    Customer customer = customerService.findOwnerDetails();
    modelMapper.map(customer, customerDTO);
    model.addAttribute("customerDTO", customerDTO);

    return "pages/profile";
  }

  @PostMapping("/update-profile")
  public String updateProfile(@Valid CustomerDTO customerDTO, BindingResult bindingResult, @AuthenticationPrincipal Customer loggedCustomer){


    // if(bindingResult.hasErrors()){
    //   return "pages/profile";
    // }

    customerService.updateCustomer(customerDTO);

    loggedCustomer.setFirstName(customerDTO.getFirstName());
    loggedCustomer.setLastName(customerDTO.getLastName());

    return "redirect:/home";
  }

  
}

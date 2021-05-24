package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.objects.CustomerDTO;
import com.hubert.crudlogin.service.CustomerService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {

  private final CustomerService customerService;
  private final ModelMapper modelMapper;
  private static final Logger log = LoggerFactory.getLogger(
    CustomerController.class
  );

  @Autowired
  public CustomerController(
    CustomerService customerService,
    ModelMapper modelMapper
  ) {
    this.customerService = customerService;
    this.modelMapper = modelMapper;
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
  public String showProfile(
    Model model,
    Authentication authentication,
    CustomerDTO customerDTO
  ) {
    Customer customer = customerService.findOwnerDetails();
    modelMapper.map(customer, customerDTO);
    model.addAttribute("customerDTO", customerDTO);
    return "pages/profile";
  }

  @PostMapping("/update-profile")
  public String updateProfile(
    @Valid CustomerDTO customerDTO,
    BindingResult bindingResult,
    @AuthenticationPrincipal Customer loggedCustomer,
    @RequestParam("profileImage") MultipartFile multipartFile,
    Model model
  )
    throws IOException {
    if (customerDTO.getUsername() == null) {
      bindingResult.addError(
        new FieldError("customerDTO", "username", "Username is empty")
      );
      return "pages/profile";
    }
    String fileName = StringUtils.cleanPath(
      multipartFile.getOriginalFilename()
    );
    if (fileName.isEmpty()) {
      customerService.updateCustomer(customerDTO);
    } else {
      if (
        multipartFile.getContentType().equalsIgnoreCase("image/jpg") ||
        multipartFile.getContentType().equalsIgnoreCase("image/jpeg") ||
        multipartFile.getContentType().equalsIgnoreCase("image/png")
      ) {
        double fileSize = multipartFile.getSize();

        double kl = (fileSize / 1024);
        double mb = (kl / 1024);
        if (mb < 5) {
          Customer existingCustomer = customerService.findOwnerDetails();
          String uploadDir = "./uploads/" + existingCustomer.getEmail();
          Path uploadPath = Paths.get(uploadDir);
          String oldFileLocation =
            uploadDir + "/" + existingCustomer.getProfileImage();
          Path getImage = Paths.get(oldFileLocation);

          if (Files.exists(getImage)) {
            Files.delete(getImage);
            System.out.println("Files deleted");
          }

          if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
          }
          try (InputStream inputStream = multipartFile.getInputStream()) {
            String newFileName = System.currentTimeMillis() + "_" + fileName;
            customerDTO.setProfileImage(newFileName);
            customerService.updateCustomer(customerDTO);
            loggedCustomer.setProfileImage(customerDTO.getProfileImage());
            Path filePath = uploadPath.resolve(newFileName).normalize();
            Files.copy(
              inputStream,
              filePath,
              StandardCopyOption.REPLACE_EXISTING
            );
          } catch (IOException e) {
            throw new IOException("Error in uploading File!");
          }
        } else {
          model.addAttribute("errorProfile", "file is too large");
          return "pages/profile";
        }
      } else {
        model.addAttribute("errorProfile", "Please enter a valid image file");
        return "pages/profile";
      }
    }
    loggedCustomer.setFirstName(customerDTO.getFirstName());
    loggedCustomer.setLastName(customerDTO.getLastName());
    loggedCustomer.setDescription(customerDTO.getDescription());
    return "redirect:/home";
  }
}

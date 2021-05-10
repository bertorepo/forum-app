package com.hubert.crudlogin.objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


public class CustomerDTO {

  @NotBlank(message = "Enter username")
  private String username;

  @NotBlank(message = "Enter first name")
  private String firstName;

  @NotBlank(message = "Enter last name")
  private String lastName;

  @NotBlank(message = "Enter email address")
  @Email(message = "Enter a valid Email address")
  private String email;

  @NotBlank(message = "Enter password")
  @Length(min = 6, message = "Password must be at least 6 characters")
  private String password;

  @NotBlank(message = "Re Enter password")
  private String rPassword;

  public CustomerDTO() {}

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
   
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    // String capitalizeName = capitalize(lastName);
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getrPassword() {
    return rPassword;
  }

  public void setrPassword(String rPassword) {
    this.rPassword = rPassword;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return (
      "CustomerDTO [email=" +
      email +
      ", firstName=" +
      firstName +
      ", lastName=" +
      lastName +
      ", password=" +
      password +
      ", rPassword=" +
      rPassword +
      "]"
    );
  }
}


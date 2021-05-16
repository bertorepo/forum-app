package com.hubert.crudlogin.objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

public class CustomerDTO {

  private Long id;

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

  @Nullable
  private String profileImage;
  
  @Nullable
  private String description;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getProfileImage() {
    return profileImage;
  }

  public void setProfileImage(String profileImage) {
    this.profileImage = profileImage;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "CustomerDTO [description=" + description + ", email=" + email + ", firstName=" + firstName + ", id=" + id
        + ", lastName=" + lastName + ", password=" + password + ", profileImage=" + profileImage + ", rPassword="
        + rPassword + ", username=" + username + "]";
  }

 
 
}

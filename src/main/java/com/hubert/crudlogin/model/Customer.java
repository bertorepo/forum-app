package com.hubert.crudlogin.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@DynamicUpdate
@Table(name = "customer")
public class Customer implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String email;
  private String password;

  @Column(name = "created_at")
  @CreationTimestamp
  private Timestamp createdAt;

  private boolean enabled;

  @Column(name = "profile_img", nullable = true)
  private String profileImage;

  @Column(name = "customer_description", nullable = true)
  private String description;

  @OneToMany(
    mappedBy = "customer",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Post> posts;

  @OneToMany(
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  @JoinTable(
    name = "customer_authority",
    joinColumns = @JoinColumn(
      name = "customer_id",
      referencedColumnName = "id"
    ),
    inverseJoinColumns = @JoinColumn(
      name = "authority_id",
      referencedColumnName = "id"
    )
  )
  private List<Authority> authorities;

  public Customer() {}

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> auth = new HashSet<>();
    authorities.forEach(
      r -> auth.add(new SimpleGrantedAuthority(r.getAuthority()))
    );
    return auth;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setUsername(String username) {
    this.username = username;
  }

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
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setAuthorities(List<Authority> authorities) {
    this.authorities = authorities;
  }

  public String getFullName() {
    return this.firstName + " " + this.lastName;
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

  public List<Post> getPosts() {
    return posts;
  }

  public void setPosts(List<Post> posts) {
    this.posts = posts;
  }

  @Transient
  public String getProfileImagePath() {
    if (profileImage == null) {
      return "/uploads/default/default.jpg";
    }

    return "/uploads/" + getEmail() + "/" + getProfileImage();
  }

  @Override
  public String toString() {
    return (
      "Customer [authorities=" +
      authorities +
      ", createdAt=" +
      createdAt +
      ", description=" +
      description +
      ", email=" +
      email +
      ", enabled=" +
      enabled +
      ", firstName=" +
      firstName +
      ", id=" +
      id +
      ", lastName=" +
      lastName +
      ", password=" +
      password +
      ", posts=" +
      posts +
      ", profileImage=" +
      profileImage +
      ", username=" +
      username +
      "]"
    );
  }
}

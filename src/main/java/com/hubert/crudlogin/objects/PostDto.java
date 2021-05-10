package com.hubert.crudlogin.objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.hubert.crudlogin.model.Customer;

public class PostDto {
  
  private Customer customer;

  @NotBlank(message = "Please enter post title")
  private String title;

  @NotEmpty(message = "Please enter content post")
  private String content;

  public PostDto() {
  }

  public PostDto(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  @Override
  public String toString() {
    return "PostDto [content=" + content + ", customer=" + customer + ", title=" + title + "]";
  }

  
  
}

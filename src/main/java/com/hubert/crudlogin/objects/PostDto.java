package com.hubert.crudlogin.objects;

import com.hubert.crudlogin.model.Customer;

public class PostDto {
  
  private Customer customer;

  private String title;
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

  
  
}

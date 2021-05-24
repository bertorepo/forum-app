package com.hubert.crudlogin.objects;

import com.hubert.crudlogin.model.Category;
import com.hubert.crudlogin.model.Customer;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public class PostDto {

  private Customer customer;
  private Category category;

  private long id;

  @NotBlank(message = "Please enter post title")
  private String title;

  @NotEmpty(message = "Please enter content post")
  private String content;

  private int viewCounts;

  public PostDto() {}

  public PostDto(Long id, String title, String content) {
    this.id = id;
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

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  

  public int getViewCounts() {
    return viewCounts;
  }

  public void setViewCounts(int viewCounts) {
    this.viewCounts = viewCounts;
  }

  @Override
  public String toString() {
    return "PostDto [category=" + category + ", content=" + content + ", customer=" + customer + ", id=" + id
        + ", title=" + title + ", viewCounts=" + viewCounts + "]";
  }

 
}

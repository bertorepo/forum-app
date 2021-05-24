package com.hubert.crudlogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Post extends AuditableBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "post_title", nullable = false)
  private String title;

  @Column(name = "post_content")
  private String content;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @JsonIgnore
  private Customer customer;

  @Column(name = "view_count")
  private int viewCounts;

  @ManyToOne
  @JoinColumn(name = "category_id")
  private Category category;

  public Post() {}

  public Post(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public int getViewCounts() {
    return viewCounts;
  }

  public void setViewCounts(int viewCounts) {
    this.viewCounts = viewCounts;
  }

  @Override
  public String toString() {
    return (
      "Post [category=" +
      category +
      ", content=" +
      content +
      ", customer=" +
      customer +
      ", id=" +
      id +
      ", title=" +
      title +
      ", viewCounts=" +
      viewCounts +
      "]"
    );
  }
}

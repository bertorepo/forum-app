package com.hubert.crudlogin.repository;

import java.util.List;

import com.hubert.crudlogin.model.Category;
import com.hubert.crudlogin.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  
  public List<Post> findByCategory(Category category);
}

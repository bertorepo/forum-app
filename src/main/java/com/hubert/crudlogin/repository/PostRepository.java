package com.hubert.crudlogin.repository;

import java.util.List;

import com.hubert.crudlogin.model.Category;
import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  
  @Query(value = "SELECT u from Post u WHERE u.category.id = :category_id")
  public List<Post> findByCategorySortedByDate(@Param("category_id") Long category_id, Sort sort);

  @Query(value = "SELECT u from Post u")
  public List<Post> findAllPostSortedByCreatedDate(Sort sort);

  @Query(value = "SELECT u from Post u WHERE u.customer.id = :customer_id")
  public List<Post> findMyPost(@Param("customer_id") Long customer_id, Sort sort);
}

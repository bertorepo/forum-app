package com.hubert.crudlogin.repository;

import com.hubert.crudlogin.model.Post;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  
  @Query(value = "SELECT u from Post u WHERE u.category.id = :category_id")
  public Page<Post> findByCategorySortedByDate(
    @Param("category_id") Long category_id,
    Pageable pageable
  );

  // @Query(value = "SELECT u from Post u")
  // public List<Post> findAllPostSortedByCreatedDate(Pageable pageable);

  @Query(value = "SELECT u from Post u WHERE u.customer.id = :customer_id")
  public Page<Post> findMyPost(
    @Param("customer_id") Long customer_id, Pageable pageable);

  @Query(value = "SELECT u from Post u WHERE u.title LIKE %:query%")
  public List<Post> findTop2ByTitleOrderByCreatedDateDesc(String query); 
}

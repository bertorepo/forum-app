package com.hubert.crudlogin.repository;

import com.hubert.crudlogin.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByName(String name);
}

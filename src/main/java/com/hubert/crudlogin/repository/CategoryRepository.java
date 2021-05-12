package com.hubert.crudlogin.repository;

import java.util.List;

import com.hubert.crudlogin.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
  
  Category findByName(String name);
}

package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Category;
import com.hubert.crudlogin.repository.CategoryRepository;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  @Autowired
  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  @Transactional
  public List<Category> allCategories() {
    return categoryRepository.findAll();
  }

  @Transactional
  public Category findCategory(String name) {
    return categoryRepository.findByName(name);
  }
}

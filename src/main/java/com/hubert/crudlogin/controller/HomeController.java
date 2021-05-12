package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.PostService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  private Logger log = LoggerFactory.getLogger(HomeController.class);

  private final PostService postService;
  private final CategoryService categoryService;

  
  @Autowired
  public HomeController(PostService postService, CategoryService categoryService) {
    this.postService = postService;
    this.categoryService = categoryService;

  }

  @GetMapping("/")
  public String index(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "index";
  }

  @GetMapping(value = "/home")
  public String home(Model model){


   log.info("Post>>" + postService.allPost().toString());
    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("allPosts", postService.allPost());
    return "pages/home";
  }

  @GetMapping(value = "/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "login";
  }

}

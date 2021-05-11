package com.hubert.crudlogin.controller;

import javax.validation.Valid;

import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.PostService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {


  private final PostService postService;
  private final CategoryService categoryService;
  private static final Logger log = LoggerFactory.getLogger(PostController.class);
  
  @Autowired
  public PostController(PostService postService, CategoryService categoryService) {
    this.postService = postService;
    this.categoryService = categoryService;
  }

  @InitBinder
  public void stringTrimmerEditor(WebDataBinder binder){
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  @GetMapping("/post")
  public String allPost(){
    return "post";
  }

  @GetMapping("/create-post")
  public String showCreatePost(@ModelAttribute PostDto postDto, 
  Authentication authentication, Model model){

    model.addAttribute("postDto", postDto);
    model.addAttribute("categoryList", categoryService.allCategories());
    return authentication == null ? "redirect:/authenticated" : "create-post";
  }

  @PostMapping("/create-post")
  public String savePost(@Valid @ModelAttribute PostDto postDto, BindingResult bindingResult, Model model){

    model.addAttribute("categoryList", categoryService.allCategories());
    if(bindingResult.hasErrors()){
      return "create-post";
    }

    log.info("post dto >> " + postDto.toString());
    
    postService.createPost(postDto);
    return "redirect:/home";

  }
}

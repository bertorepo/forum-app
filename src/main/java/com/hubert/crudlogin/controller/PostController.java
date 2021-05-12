package com.hubert.crudlogin.controller;

import javax.validation.Valid;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.PostService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PostController implements ErrorController {


  private final PostService postService;
  private final CategoryService categoryService;
  private static final Logger log = LoggerFactory.getLogger(PostController.class);

  private static final String PATH = "/error";
  
  @Autowired
  public PostController(PostService postService, CategoryService categoryService) {
    this.postService = postService;
    this.categoryService = categoryService;
  }

  @RequestMapping(value = PATH)
  public String error(){
    return PATH;
  }

  @Override
  public String getErrorPath() {
  
    return PATH;
  }



  @InitBinder
  public void stringTrimmerEditor(WebDataBinder binder){
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  @GetMapping("/create-post")
  public String showCreatePost(@ModelAttribute PostDto postDto, 
  Authentication authentication, Model model){

    model.addAttribute("postDto", postDto);
    model.addAttribute("categoryList", categoryService.allCategories());
    return authentication == null ? "redirect:/home" : "pages/post/create-post";
  }

  @PostMapping("/create-post")
  public String savePost(@Valid @ModelAttribute PostDto postDto, BindingResult bindingResult, Model model){

    model.addAttribute("categoryList", categoryService.allCategories());
    if(bindingResult.hasErrors()){
      return "pages/post/create-post";
    }

    log.info("post dto >> " + postDto.toString());
    
    postService.createPost(postDto);
    return "redirect:/home";

  }

  //view individual post
  @RequestMapping("/post/{id}")
  public String viewPost(@PathVariable("id") int id, Model model){

    Post post = postService.showPost(id);
    
    if(post == null){
      return "redirect:" + PATH;
    }
    model.addAttribute("myPost", post);

    return "pages/post/view-post";
  }
}

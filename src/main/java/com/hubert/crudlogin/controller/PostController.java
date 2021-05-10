package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

  private final PostService postService;

  
  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/post")
  public String allPost(){
    return "post";
  }

  @GetMapping("/create-post")
  public String showCreatePost(@ModelAttribute PostDto postDto, 
  Authentication authentication, Model model){

    model.addAttribute("postDto", postDto);
    return authentication == null ? "redirect:/authenticated" : "create-post";
  }

  @PostMapping("/create-post")
  public String savePost(PostDto postDto){

    postService.createPost(postDto);
    return "redirect:/home";
  }
}

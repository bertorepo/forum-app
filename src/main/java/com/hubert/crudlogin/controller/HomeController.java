package com.hubert.crudlogin.controller;

import java.util.List;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
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

  
  @Autowired
  public HomeController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("/")
  public String index(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "index";
  }

  @GetMapping("/home")
  public String home(Model model){

    List<Post> allPosts = postService.allPost();

   log.info("Post>>" + allPosts.toString());
   
    model.addAttribute("allPosts", allPosts);
    return "home";
  }

  @GetMapping("/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "login";
  }

}

package com.hubert.crudlogin.controller;

import java.util.List;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final PostService postService;

  
  @Autowired
  public AdminController(PostService postService) {
    this.postService = postService;
  }

  @GetMapping("")
    public String showAdminPage(Authentication authentication){
   return authentication != null ? "pages/admin/admin" : "redirect:/login";
    }

  @GetMapping("/manage-post")
  public String showAllPost(Model model){

    return findPaginatedPost(1, model);
    
  }


  //pagination for mambers admin area

  @GetMapping("/page/{pageNumber}")
  public String findPaginatedPost(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model
  ) {
    //initial page size
    int pageSize = 10;

    Page<Post> page = postService.paginateList(pageNumber, pageSize);
    
    List<Post> membersPosts = page.getContent();

    
    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("totalPostCount", postService.totalPostCount());

  
    model.addAttribute("membersPosts", membersPosts);

    return "pages/admin/manage_posts";
  }

  
}

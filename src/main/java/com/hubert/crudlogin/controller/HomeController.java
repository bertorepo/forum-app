package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

  // private Logger log = LoggerFactory.getLogger(HomeController.class);

  private final PostService postService;
  private final CategoryService categoryService;

  @Autowired
  public HomeController(
    PostService postService,
    CategoryService categoryService
  ) {
    this.postService = postService;
    this.categoryService = categoryService;
  }

  @GetMapping("/")
  public String index(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "index";

    
  }

  @GetMapping(value = "/home")
  public String home(Model model) {
    return findPaginatedPost(1, model);
  }

  @GetMapping(value = "/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "login";
  }

  //pagination

  @GetMapping("/page/{pageNumber}")
  public String findPaginatedPost(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model
  ) {
    //initial page size
    int pageSize = 10;

    Page<Post> page = postService.paginateList(pageNumber, pageSize);
    List<Post> allPosts = page.getContent();

    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());

    //passing paginated post
    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("allPosts", allPosts);

    return "pages/home";
  }
}

package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.CustomerService;
import com.hubert.crudlogin.service.PostService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController{

  // private Logger log = LoggerFactory.getLogger(HomeController.class);

  private final PostService postService;
  private final CategoryService categoryService;
  private final CustomerService customerService;

  // private static final String PATH = "/error";

  @Autowired
  public HomeController(
    PostService postService,
    CategoryService categoryService,
    CustomerService customerService
  ) {
    this.postService = postService;
    this.categoryService = categoryService;
    this.customerService = customerService;
  }

  
  // @RequestMapping(value = PATH)
  // public String error() {
  //   return PATH;
  // }

  // //return error page for any error occurence
  // @Override
  // public String getErrorPath() {
  //   return PATH;
  // }

@GetMapping("favicon.ico")
@ResponseBody
public void disableFavicon() {
 //Method is void to avoid browser 404 issue by returning nothing in the response.
}

  @GetMapping("/")
  public String index(Authentication authentication, Model model, @Param("query") String query) {
    return authentication != null ? "redirect:/home" : findPaginatedPostIndex(1, model, query);

    
  }

  @GetMapping(value = "/home")
  public String home(Model model, @Param("query") String query) {
    return findPaginatedPost(1, model, query);
  }

  @GetMapping(value = "/login")
  public String login(Authentication authentication) {
    return authentication != null ? "redirect:/home" : "login";
  }

  //pagination

  @GetMapping("/page/{pageNumber}")
  public String findPaginatedPost(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model, String query
  ) {
    //initial page size
    int pageSize = 10;

    Page<Post> page = postService.paginateList(pageNumber, pageSize, query);
    List<Post> allPosts = page.getContent();

    String nav = "home";
    //link activer
    model.addAttribute("navActive", nav);
    
    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("totalPostCount", postService.totalPostCount());
    model.addAttribute("totalCustomerCount", customerService.countAllCustomer());

  

    //passing paginated post
    model.addAttribute("query", query);
    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("allPosts", allPosts);

    return "pages/home";
  }


@GetMapping("/{pageNumber}")
  public String findPaginatedPostIndex(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model, String query
  ) {
    //initial page size
    int pageSize = 15;

    Page<Post> page = postService.paginateList(pageNumber, pageSize, query);
    List<Post> viewPosts = page.getContent();

    String nav = "home";
    //link activer
    model.addAttribute("navActive", nav);
    
    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());

    //passing paginated post
    model.addAttribute("query", query);
    model.addAttribute("viewPosts", viewPosts);

    return "index";
  }

  @GetMapping("/view/{id}")
  public String  showPostIndex(@PathVariable("id") int id, Model model, Authentication authentication){
    Post post = postService.showPost(id);

    model.addAttribute("indexPost", post);
    return authentication == null ? "view-post" : "redirect:/home";
  }
}



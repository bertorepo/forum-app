package com.hubert.crudlogin.controller;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.CustomerService;
import com.hubert.crudlogin.service.PostService;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController implements ErrorController {

  private final PostService postService;
  private final CategoryService categoryService;
  private ModelMapper modelMapper;
  private final CustomerService customerService;
  private static final Logger log = LoggerFactory.getLogger(
    PostController.class
  );

  private static final String PATH = "/error";

  @Autowired
  public PostController(
    PostService postService,
    CategoryService categoryService,
    ModelMapper modelMapper,
    CustomerService customerService
  ) {
    this.postService = postService;
    this.categoryService = categoryService;
    this.modelMapper = modelMapper;
    this.customerService = customerService;
  }

  @RequestMapping(value = PATH)
  public String error() {
    return PATH;
  }

  //return error page for any error occurence
  @Override
  public String getErrorPath() {
    return PATH;
  }

  @InitBinder
  public void stringTrimmerEditor(WebDataBinder binder) {
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  @GetMapping("/create-post")
  public String showCreatePost(
    @ModelAttribute("postDto") PostDto postDto,
    Authentication authentication,
    Model model
  ) {
    String nav = "create-post";
    //link activer
    model.addAttribute("navActive", nav);
    model.addAttribute("postDto", postDto);
    model.addAttribute("categoryList", categoryService.allCategories());
    return "pages/post/create-post";
  }

  @PostMapping("/save-post")
  public String savePost(
    @Valid @ModelAttribute("postDto") PostDto postDto,
    BindingResult bindingResult,
    Model model
  ) {
    model.addAttribute("categoryList", categoryService.allCategories());
    if (bindingResult.hasErrors()) {
      return "pages/post/create-post";
    }

    log.info("post dto >> " + postDto.toString());

    postService.createPost(postDto);
    return "redirect:/home";
  }

   //get customer login post
   @RequestMapping("/my-post")
   public String getMyPost(Model model) {
    
     return getMyPost(1, model);
   }
 

  //get customer login post
  @GetMapping("/my-post/{pageNumber}")
  public String getMyPost(@PathVariable(value = "pageNumber") int pageNumber, Model model) {

    int pageSize = 5;

    Page<Post> page = postService.findMyPost(pageNumber, pageSize);
    String nav = "my-post";
    //link activer
    model.addAttribute("navActive", nav);

      //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());

    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("myPostList", page.getContent());
    model.addAttribute("totalPostCount", postService.totalPostCount());
    model.addAttribute("totalCustomerCount", customerService.countAllCustomer());


    return "pages/post/my-post";
  }

  //edit post
  @RequestMapping("/edit-post/{id}")
  public ModelAndView editPost(
    @PathVariable(name = "id") int id,
    Model model,
    @ModelAttribute("postDto") PostDto postDto
  ) {
    ModelAndView mav = new ModelAndView("pages/post/create-post");

    Post post = postService.showPost(id);
    modelMapper.map(post, postDto);

    postDto.setId(post.getId());

    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("postDto", postDto);
    mav.addObject(postDto);

    // log.info("postDTO >>" + postDto.toString());

    return mav;
  }

  //view individual post
  @RequestMapping("/post/{id}")
  public String viewPost(@PathVariable("id") int id, Model model) {
    Post post = postService.showPost(id);

    if (post == null) {
      return "redirect:" + PATH;
    }
    model.addAttribute("myPost", post);
    return "pages/post/view-post";
  }

  @RequestMapping("/category/{category_name}")
  public String showByCategory(
    @PathVariable("category_name") String name,
    Model model
  ) {

    int pageSize = 9;

    Page<Post> page = postService.findPostByCategory(name, 1, pageSize);

    List<Post> categoryPosts = page.getContent();

    //make button active
    for (Post getPost : categoryPosts) {
      if (getPost.getCategory().getName().equals(name)) {
        model.addAttribute("activeCategory", getPost.getCategory().getName());
      }
    }

    //passing pagination attribute
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());

    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("categoryPosts", categoryPosts);
    model.addAttribute("totalPostCount", postService.totalPostCount());
    model.addAttribute("totalCustomerCount", customerService.countAllCustomer());

    return "pages/category/category";
  }

  //delete post

  @RequestMapping("/delete-post/{id}")
  public String deletePost(
    @PathVariable("id") int id,
    Authentication authentication
  ) {
    postService.deletePost(id);
    return authentication == null ? "redirect:/" : "redirect:/home";
  }
}

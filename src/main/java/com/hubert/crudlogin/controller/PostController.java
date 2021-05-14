package com.hubert.crudlogin.controller;

import java.util.List;

import javax.validation.Valid;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.service.CategoryService;
import com.hubert.crudlogin.service.PostService;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
  private static final Logger log = LoggerFactory.getLogger(PostController.class);

  private static final String PATH = "/error";
  
  @Autowired
  public PostController(PostService postService, CategoryService categoryService, ModelMapper modelMapper) {
    this.postService = postService;
    this.categoryService = categoryService;
    this.modelMapper = modelMapper;
  }

  @RequestMapping(value = PATH)
  public String error(){
    return PATH;
  }

  //return error page for any error occurence
  @Override
  public String getErrorPath() {
    return PATH;
  }

  private Customer getPrincipal() {
    Customer customer = null;

    if (
      SecurityContextHolder
        .getContext()
        .getAuthentication()
        .getPrincipal() instanceof Customer
    ) {
      customer =
        (Customer) SecurityContextHolder
          .getContext()
          .getAuthentication()
          .getPrincipal();
    }
    return customer;
  }



  @InitBinder
  public void stringTrimmerEditor(WebDataBinder binder){
    StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
    binder.registerCustomEditor(String.class, stringTrimmerEditor);
  }

  @GetMapping("/create-post")
  public String showCreatePost(@ModelAttribute("postDto") PostDto postDto, 
  Authentication authentication, Model model){

    model.addAttribute("postDto", postDto);
    model.addAttribute("categoryList", categoryService.allCategories());
    return "pages/post/create-post";
  }

  @PostMapping("/save-post")
  public String savePost(@Valid @ModelAttribute("postDto") PostDto postDto, BindingResult bindingResult, Model model){

    model.addAttribute("categoryList", categoryService.allCategories());
    if(bindingResult.hasErrors()){
      return "pages/post/create-post";
    }

    log.info("post dto >> " + postDto.toString());
    
    postService.createPost(postDto);
    return "redirect:/home";

  }

  //get customer login post
  @GetMapping("/my-post")
  public String getMyPost(Model  model, Authentication authentication){

    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("myPostList", postService.findMyPost());
    return "pages/post/my-post";
  }

  //edit post
  @RequestMapping("/edit-post/{id}")
  public ModelAndView editPost(@PathVariable(name = "id") int id, Model model, @ModelAttribute("postDto") PostDto postDto){
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
  public String viewPost(@PathVariable("id") int id, Model model){

    Post post = postService.showPost(id);
    
    if(post == null){
      return "redirect:" + PATH;
    }
    model.addAttribute("myPost", post);

    return "pages/post/view-post";
  }


  @RequestMapping("/category/{category_name}")
  public String showByCategory(@PathVariable("category_name") String name, Model model){

    List<Post> categoryPosts = postService.findPostByCategory(name);

    //make button active
    for(Post getPost : categoryPosts){
      if(getPost.getCategory().getName().equals(name)){
        model.addAttribute("activeCategory", getPost.getCategory().getName());
      }
    }

    model.addAttribute("categoryList", categoryService.allCategories());
    model.addAttribute("categoryPosts", categoryPosts);
    return "pages/category/category";
  }

  //delete post

  @RequestMapping("/delete-post/{id}")
  public String deletePost(@PathVariable("id") int id, Authentication authentication){

    postService.deletePost(id);
    return authentication == null ? "redirect:/" : "redirect:/home";
  }
}

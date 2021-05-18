package com.hubert.crudlogin.controller;

import java.util.List;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.CustomerService;
import com.hubert.crudlogin.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final PostService postService;
  private final CustomerService customerService;

  
  @Autowired
  public AdminController(PostService postService, CustomerService customerService) {
    this.postService = postService;
    this.customerService = customerService;
  }

  @GetMapping("")
    public String showAdminPage(Authentication authentication){
   return authentication != null ? "pages/admin/admin" : "redirect:/login";
    }

  @GetMapping("/manage-post")
  public String showAllPost(Model model){

    return findPaginatedPost(1, model);
    
  }

  @GetMapping("/manage-customers")
  public String showAllCustomers(Model model){

    return findPaginatedCustomers(1, model);
  }


  //pagination for mambers admin area

  @GetMapping("/post-page/{pageNumber}")
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
  @GetMapping("/members-page/{pageNumber}")
  public String findPaginatedCustomers(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model
  ) {
    //initial page size
    int pageSize = 24;

    Page<Customer> page = customerService.paginateList(pageNumber, pageSize);
   
    
    List<Customer> customersLists = page.getContent();

    
    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
   
    model.addAttribute("customersLists", customersLists);

    return "pages/admin/manage_customers";
  }

  //set status for members

  @GetMapping("/set-status/{customerId}")
  public String setStatus(@PathVariable(value = "customerId") int customerId){

    Customer customer = customerService.getCustomer(customerId);
    customer.setEnabled(!customer.isEnabled());

    customerService.save(customer);

    return "redirect:/admin/manage-customers";

  }
  //delete members

  @GetMapping("/delete-member/{customerId}")
  public String deleteCustomer(@PathVariable(value = "customerId") int customerId){

    customerService.deleteCustomer(customerId);
    return "redirect:/admin/manage-customers";

  }
 
  
}

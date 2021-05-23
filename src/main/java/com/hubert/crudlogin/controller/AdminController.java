package com.hubert.crudlogin.controller;

import java.util.List;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.CustomerService;
import com.hubert.crudlogin.service.PostService;

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




@Controller
@RequestMapping("/admin")
public class AdminController implements ErrorController {

  private final PostService postService;
  private final CustomerService customerService;

  private static final String PATH = "/error";
  
  @RequestMapping(value = PATH)
  public String error() {
    return PATH;
  }

  //return error page for any error occurence
  @Override
  public String getErrorPath() {
    return PATH;
  }
  
  @Autowired
  public AdminController(PostService postService, CustomerService customerService) {
    this.postService = postService;
    this.customerService = customerService;
  }

  @GetMapping()
    public String showAdminPage(Authentication authentication, Model model){
    
    String nav = "admin";
    //link active
    model.addAttribute("navActive", nav);

   return authentication != null ? "pages/admin/admin" : "redirect:/login";
    }

  @GetMapping("/manage-post")
  public String showAllPost(Model model, @Param("query") String query){

    return findPaginatedPost(1, model, query);
    
  }

  @RequestMapping("/manage-customers")
  public String showAllCustomers(Model model, @Param("query") String query){
    return findPaginatedCustomers(1, model, query);
  }
 

  //pagination for mambers admin area

  @GetMapping("/post-page/{pageNumber}")
  public String findPaginatedPost(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model, String query
  ) {
    //initial page size
    int pageSize = 10;

    Page<Post> page = postService.paginateList(pageNumber, pageSize ,query);
    List<Post> membersPosts = page.getContent();

    
    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
    model.addAttribute("totalPostCount", postService.totalPostCount());

    String nav = "admin";
    //link active
    
    model.addAttribute("navActive", nav);
    model.addAttribute("membersPosts", membersPosts);
    model.addAttribute("query", query);

    return "pages/admin/manage_posts";
  }
  

  @GetMapping("/members-page/{pageNumber}")
  public String findPaginatedCustomers(
    @PathVariable(value = "pageNumber") int pageNumber,
    Model model, String query
  ) {
    //initial page size
    int pageSize = 24;

    Page<Customer> page = customerService.paginateList(pageNumber, pageSize, query);
    List<Customer> customersLists = page.getContent();

    //passing pagination attribute
    model.addAttribute("pageNumber", pageNumber);
    model.addAttribute("totalPages", page.getTotalPages());
    model.addAttribute("totalItems", page.getTotalElements());
   
    String nav = "admin";
    //link active

    model.addAttribute("navActive", nav);
    model.addAttribute("customersLists", customersLists);
    model.addAttribute("query", query);

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

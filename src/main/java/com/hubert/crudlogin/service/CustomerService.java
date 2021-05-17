package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.objects.CustomerDTO;
import com.hubert.crudlogin.repository.CustomerRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final ModelMapper modelMapper;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public CustomerService(
    CustomerRepository customerRepository,
    ModelMapper modelMapper,
    BCryptPasswordEncoder passwordEncoder,
    AuthorityService authorityService
  ) {
    this.customerRepository = customerRepository;
    this.modelMapper = modelMapper;
    this.passwordEncoder = passwordEncoder;
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

  @Transactional
  public Optional<Customer> findCustomerByUsername(String username) {
    return customerRepository.findCustomerByUsername(username);
  }

  @Transactional
  public Optional<Customer> findCustomerByEmail(String email) {
    return customerRepository.findCustomerByEmail(email);
  }

  @Transactional
  public boolean customerExists(String email) {
    return findCustomerByEmail(email).isPresent();
  }

  @Transactional
  public Customer findOwnerDetails(){
    Optional<Customer> customer = findCustomerByEmail(getPrincipal().getEmail());

    if(!customer.isPresent()){
      throw new IllegalStateException("Customer is not found");
    }
    
    return customer.get();
  }

  @Transactional
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  @Transactional
  public long countAllCustomer(){
    return customerRepository.count();
  }

  public Customer register(CustomerDTO customerDTO) {
    //steps to map DTO to entity

    //encrypt password
    String password = passwordEncoder.encode(customerDTO.getPassword());
    customerDTO.setPassword(password);
    customerDTO.setFirstName(capitalize(customerDTO.getFirstName()));
    customerDTO.setLastName(capitalize(customerDTO.getLastName()));

    //1.instaciate a new customer object
    Customer customer = new Customer();
    //set enable to true
    customer.setEnabled(true);

    //1.map CustomerDTO to Customer Object -> add model mapper dependency to pom.xml file
    modelMapper.map(customerDTO, customer);
    return save(customer);
  }

  // capitalize First Letter String
  private String capitalize(String name) {
    return StringUtils.capitalize(name.toLowerCase());
  }

  public Customer updateCustomer(CustomerDTO customerDTO){

    Customer customer = findOwnerDetails();

    if(customerDTO.getProfileImage() !=null){
      customer.setProfileImage(customerDTO.getProfileImage());
    }

    if(customerDTO.getDescription() !=null){
      customer.setDescription(customerDTO.getDescription());
    }

    if(customerDTO.getFirstName() != null){
      customer.setFirstName(customerDTO.getFirstName());
    }

    if(customerDTO.getLastName() !=null){
      customer.setLastName(customerDTO.getLastName());
    }

    if(customerDTO.getUsername() != null){
      customer.setUsername(customerDTO.getUsername());
    }

    return save(customer);
    
  }
}

package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Authority;
import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.objects.CustomerDTO;
import com.hubert.crudlogin.repository.CustomerRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final AuthorityService authorityService;
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
    this.authorityService = authorityService;
  }

  @Transactional
  public Optional<Customer> findCustomerByUsername(String username) {
    return customerRepository.findCustomerByUsername(username);
  }

  @Transactional
  public Optional<Customer> findCustomerByEmail(String email) {
    return customerRepository.findCustomerByEmail(email);
  }

  public boolean customerExists(String email) {
    return findCustomerByEmail(email).isPresent();
  }

  @Transactional
  public Customer save(Customer customer) {
    return customerRepository.save(customer);
  }

  public Customer register(CustomerDTO customerDTO) {
    //steps to map DTO to entity

    //encrypt password
    String password = passwordEncoder.encode(customerDTO.getPassword());
    customerDTO.setPassword(password);

    //1.instaciate a new customer object
    Customer customer = new Customer();
    
    //get CUSTOMER authority in the DB and assign automatically to every member who register
    List<Authority> authorities = Arrays.asList(authorityService.findAuthorityById(2));
    customer.setAuthorities(authorities);
   

    //set enable to true
    customer.setEnabled(true);

    //1.map CustomerDTO to Customer Object -> add model mapper dependency to pom.xml file
    modelMapper.map(customerDTO, customer);
    return save(customer);
  }
}

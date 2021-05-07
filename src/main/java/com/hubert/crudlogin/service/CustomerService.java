package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.repository.CustomerRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  @Autowired
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Transactional
  public Optional<Customer> findCustomerByUsername(String username) {
    return customerRepository.findCustomerByUsername(username);
  }
}

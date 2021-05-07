package com.hubert.crudlogin.repository;

import com.hubert.crudlogin.model.Customer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  //find username in the db

  Optional<Customer> findCustomerByUsername(String username);
}

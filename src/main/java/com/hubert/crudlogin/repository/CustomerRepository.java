package com.hubert.crudlogin.repository;

import com.hubert.crudlogin.model.Customer;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  //find username in the db

  Optional<Customer> findCustomerByUsername(String username);
  Optional<Customer> findCustomerByEmail(String email);

  @Query(value = "SELECT u from Customer u WHERE u.firstName LIKE %:query% or u.lastName LIKE %:query%")
  Page<Customer> findAll(@Param("query") String query, Pageable pageable);
}

package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Authority;
import com.hubert.crudlogin.repository.AuthorityRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService {

  private final AuthorityRepository authorityRepository;

  @Autowired
  public AuthorityService(AuthorityRepository authorityRepository) {
    this.authorityRepository = authorityRepository;
  }

  @Transactional
  public Authority findAuthorityById(long id) {
    return authorityRepository.findById(id).get();
  }
}

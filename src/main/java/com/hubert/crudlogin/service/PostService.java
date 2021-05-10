package com.hubert.crudlogin.service;
import javax.transaction.Transactional;

import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.repository.PostRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {
  
  private PostRepository postRepository;
  private ModelMapper modelMapper;


  
  @Autowired
  public PostService(PostRepository postRepository, ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.modelMapper = modelMapper;
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
  public Post save(Post post){
    return postRepository.save(post);
  }


  public Post createPost(PostDto postDto){
    Customer customer = getPrincipal();
    Post post  = new Post();
    postDto.setCustomer(customer);
    modelMapper.map(postDto, post);

    return save(post);
  }
}

package com.hubert.crudlogin.controller;

import java.util.List;

import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {


  private final PostService postService;
  
  @Autowired
    public SearchController(PostService postService) {
    this.postService = postService;
  }


    @GetMapping("/search-post/{query}")
    public ResponseEntity<?> searchPost(@PathVariable("query") String query){

      List<Post> listOfSearchPosts = postService.searchPost(query);
      return ResponseEntity.ok(listOfSearchPosts);
    }
}

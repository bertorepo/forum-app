package com.hubert.crudlogin.service;

import com.hubert.crudlogin.model.Category;
import com.hubert.crudlogin.model.Customer;
import com.hubert.crudlogin.model.Post;
import com.hubert.crudlogin.objects.PostDto;
import com.hubert.crudlogin.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private PostRepository postRepository;
  private CategoryService categoryService;
  private ModelMapper modelMapper;

  @Autowired
  public PostService(
    PostRepository postRepository,
    CategoryService categoryService,
    ModelMapper modelMapper
  ) {
    this.postRepository = postRepository;
    this.modelMapper = modelMapper;
    this.categoryService = categoryService;
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

  public Post save(Post post) {
    return postRepository.save(post);
  }

  @Transactional
  public List<Post> allPost() {
    return postRepository.findAll();
  }

  @Transactional
  public Post showPost(long id) {
    Optional<Post> findPost = postRepository.findById(id);
    if (!findPost.isPresent()) {
      throw new IllegalStateException("No Post found in this " + id);
    }
    return findPost.orElseThrow();
  }

  @Transactional
  public Page<Post> findPostByCategory(
    String name,
    int pageNumber,
    int pageSize
  ) {
    Category category = categoryService.findCategory(name);
    Pageable page = PageRequest.of(
      pageNumber - 1,
      pageSize,
      Sort.by(Sort.Direction.DESC, "createdDate")
    );
    return postRepository.findByCategorySortedByDate(category.getId(), page);
  }

  @Transactional
  public void deletePost(long id) {
    Optional<Post> findPost = postRepository.findById(id);
    if (!findPost.isPresent()) {
      throw new IllegalStateException("No Post found in this " + id);
    }

    postRepository.deleteById(id);
  }

  @Transactional
  public long totalPostCount() {
    return postRepository.count();
  }

  @Transactional
  public Page<Post> findMyPost(int pageNumber, int pageSize) {
    Pageable page = PageRequest.of(
      pageNumber - 1,
      pageSize,
      Sort.by(Sort.Direction.DESC, "createdDate")
    );
    return postRepository.findMyPost(getPrincipal().getId(), page);
  }

  public Post createPost(PostDto postDto) {
    Customer customer = getPrincipal();
    Category category = postDto.getCategory();

    Post post = new Post();
    postDto.setViewCounts(postDto.getViewCounts());
    postDto.setCustomer(customer);
    postDto.setCategory(category);
    modelMapper.map(postDto, post);
    return save(post);
  }

  //pagination for all post

  @Transactional
  public Page<Post> paginateList(int pageNumber, int pageSize, String query) {
    Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    if (query != null) {
      return postRepository.findPostByTitle(query, pageable);
    }
    return postRepository.findAll(pageable);
  }

  //search Post
  @Transactional
  public List<Post> searchPost(String query) {
    return postRepository.findByTitleAnPosts(query);
  }

  @Transactional
  public Long countPostByCategory(long id) {
    Long totalCount = postRepository.countPostByCategory(id);

    if (totalCount == 0) {
      return 0L;
    }

    return totalCount;
  }
}

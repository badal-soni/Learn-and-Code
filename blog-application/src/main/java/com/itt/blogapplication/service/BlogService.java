package com.itt.blogapplication.service;

import com.itt.blogapplication.constant.ErrorMessages;
import com.itt.blogapplication.dto.request.BlogRequest;
import com.itt.blogapplication.dto.response.BlogResponse;
import com.itt.blogapplication.entity.Blog;
import com.itt.blogapplication.exception.NotFoundException;
import com.itt.blogapplication.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;

    public BlogResponse createBlog(BlogRequest blogRequest) {
        Blog blog = modelMapper.map(blogRequest, Blog.class);
        blog = blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponse.class);
    }

    public List<BlogResponse> findAllBlogs() {
        return blogRepository
                .findAll()
                .stream()
                .map(blog -> modelMapper.map(blog, BlogResponse.class))
                .toList();
    }

    public BlogResponse findBlogById(UUID id) {
        Optional<Blog> blog = blogRepository.findById(id);
        if (blog.isPresent()) {
            return modelMapper.map(blog.get(), BlogResponse.class);
        }
        throw new NotFoundException(String.format(ErrorMessages.BLOG_NOT_FOUND, id));
    }

    public BlogResponse updateBlogById(UUID id, BlogRequest blogRequest) {
        Blog blog = modelMapper.map(blogRequest, Blog.class);
        blog.setId(id);
        blog = blogRepository.save(blog);
        return modelMapper.map(blog, BlogResponse.class);
    }

    public void deleteBlogById(UUID id) {
        blogRepository.deleteById(id);
    }

}

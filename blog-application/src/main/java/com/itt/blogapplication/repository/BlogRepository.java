package com.itt.blogapplication.repository;

import com.itt.blogapplication.constant.ErrorMessages;
import com.itt.blogapplication.entity.Blog;
import com.itt.blogapplication.exception.NotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BlogRepository {

    private final Map<UUID, Blog> blogIdToBlog = new HashMap<>();

    public List<Blog> findAll() {
        return blogIdToBlog
                .values()
                .stream()
                .toList();
    }

    public Optional<Blog> findById(UUID id) {
        return Optional.ofNullable(blogIdToBlog.get(id));
    }

    public Blog save(Blog blog) {
        return blogIdToBlog.containsKey(blog.getId()) ? update(blog) : insert(blog);
    }

    private Blog update(Blog blogToUpdate) {
        Blog savedBlog = blogIdToBlog.get(blogToUpdate.getId());
        savedBlog.setTitle(blogToUpdate.getTitle());
        savedBlog.setContent(blogToUpdate.getContent());
        savedBlog.setUpdatedAt(LocalDateTime.now());
        blogIdToBlog.put(savedBlog.getId(), savedBlog);
        return savedBlog;
    }

    private Blog insert(Blog blog) {
        blog.setId(UUID.randomUUID());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setUpdatedAt(LocalDateTime.now());
        blogIdToBlog.put(blog.getId(), blog);
        return blog;
    }

    public void deleteById(UUID id) {
        Optional<Blog> blogOptional = findById(id);
        if (blogOptional.isPresent()) {
            blogIdToBlog.remove(blogOptional.get().getId());
        } else {
            throw new NotFoundException(String.format(ErrorMessages.CANNOT_DELETE_BLOG, id));
        }
    }

}

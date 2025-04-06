package com.itt.blogapplication.controller;

import com.itt.blogapplication.constant.BaseUrls;
import com.itt.blogapplication.dto.request.BlogRequest;
import com.itt.blogapplication.dto.response.ApiSuccessResponse;
import com.itt.blogapplication.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(
        path = BaseUrls.V1_BLOGS,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiSuccessResponse> createBlog(@RequestBody BlogRequest blogRequest) {
        return ApiSuccessResponse
                .builder()
                .data(blogService.createBlog(blogRequest))
                .message(HttpStatus.CREATED.getReasonPhrase())
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    @PutMapping(
            path = "/{blogId}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiSuccessResponse> updateBlog(
            @RequestBody BlogRequest blogRequest,
            @PathVariable UUID blogId
    ) {
        return ApiSuccessResponse
                .builder()
                .data(blogService.updateBlogById(blogId, blogRequest))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse> findAllBlogs() {
        return ApiSuccessResponse
                .builder()
                .data(blogService.findAllBlogs())
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @GetMapping(path = "/{blogId}")
    public ResponseEntity<ApiSuccessResponse> findBlogById(@PathVariable UUID blogId) {
        return ApiSuccessResponse
                .builder()
                .data(blogService.findBlogById(blogId))
                .message(HttpStatus.OK.getReasonPhrase())
                .httpStatus(HttpStatus.OK)
                .build();
    }

    @DeleteMapping(path = "/{blogId}")
    public ResponseEntity<ApiSuccessResponse> deleteBlog(@PathVariable UUID blogId) {
        blogService.deleteBlogById(blogId);
        return ApiSuccessResponse
                .builder()
                .httpStatus(HttpStatus.NO_CONTENT)
                .build();
    }

}

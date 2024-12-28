package com.davidng.app.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.davidng.app.dto.PostCreateReq;
import com.davidng.app.dto.PostResp;
import com.davidng.app.entity.Post;
import com.davidng.app.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

public class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidPostRequest_whenCreatePost_thenReturnPostResponse() {
        PostCreateReq req = PostCreateReq.builder()
                .title("Test Title")
                .content("Test Content")
                .slug("test-title")
                .build();

        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setTitle(req.getTitle());
        savedPost.setContent(req.getContent());
        savedPost.setSlug(req.getSlug());

        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostResp response = postService.createPost(req);

        assertNotNull(response);
        assertEquals(savedPost.getId(), response.getId());
        assertEquals(savedPost.getTitle(), response.getTitle());
        assertEquals(savedPost.getContent(), response.getContent());
        assertEquals(savedPost.getSlug(), response.getSlug());

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void givenPostsExist_whenGetAllPosts_thenReturnListOfPostResponses() {
        List<Post> posts = new ArrayList<>();

        Post post1 = new Post();
        post1.setId(1L);
        post1.setTitle("Title 1");
        post1.setContent("Content 1");
        post1.setSlug("title-1");
        post1.setComments(new ArrayList<>());

        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Title 2");
        post2.setContent("Content 2");
        post2.setSlug("title-2");
        post2.setComments(new ArrayList<>());

        posts.add(post1);
        posts.add(post2);

        when(postRepository.findAll()).thenReturn(posts);

        List<PostResp> responses = postService.getAllPosts();

        assertNotNull(responses);
        assertEquals(2, responses.size());

        assertEquals(post1.getId(), responses.get(0).getId());
        assertEquals(post1.getTitle(), responses.get(0).getTitle());
        assertEquals(post1.getContent(), responses.get(0).getContent());
        assertEquals(post1.getSlug(), responses.get(0).getSlug());

        assertEquals(post2.getId(), responses.get(1).getId());
        assertEquals(post2.getTitle(), responses.get(1).getTitle());
        assertEquals(post2.getContent(), responses.get(1).getContent());
        assertEquals(post2.getSlug(), responses.get(1).getSlug());

        verify(postRepository, times(1)).findAll();
    }
}

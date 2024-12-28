package com.davidng.app.service;

import com.davidng.app.dto.CommentCreateReq;
import com.davidng.app.dto.CommentResp;
import com.davidng.app.entity.Comment;
import com.davidng.app.entity.Post;
import com.davidng.app.exception.PostNotFoundException;
import com.davidng.app.repository.CommentRepository;
import com.davidng.app.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidCommentRequest_whenCreateComment_thenReturnCommentResponse() {
        CommentCreateReq req = CommentCreateReq.builder()
                .content("Test Comment")
                .postId(1L)
                .build();

        Post post = new Post();
        post.setId(1L);

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setContent(req.getContent());
        savedComment.setPost(post);

        when(postRepository.findById(req.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentResp response = commentService.createComment(req);

        assertNotNull(response);
        assertEquals(savedComment.getId(), response.getId());
        assertEquals(savedComment.getContent(), response.getContent());
        assertEquals(savedComment.getPost().getId(), response.getPostId());

        verify(postRepository, times(1)).findById(req.getPostId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void givenNonexistentPostId_whenCreateComment_thenThrowPostNotFoundException() {
        CommentCreateReq req = CommentCreateReq.builder()
                .content("Test Comment")
                .postId(1L)
                .build();

        when(postRepository.findById(req.getPostId())).thenReturn(Optional.empty());

        PostNotFoundException exception = assertThrows(PostNotFoundException.class, () -> {
            commentService.createComment(req);
        });

        assertEquals("Comment should belong on a post, but post not found with id 1", exception.getMessage());

        verify(postRepository, times(1)).findById(req.getPostId());
        verify(commentRepository, never()).save(any(Comment.class));
    }
}

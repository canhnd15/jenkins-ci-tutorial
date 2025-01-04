package com.davidng.app.service;

import com.davidng.app.dto.CommentCreateReq;
import com.davidng.app.dto.CommentResp;
import com.davidng.app.entity.Comment;
import com.davidng.app.entity.Post;
import com.davidng.app.exception.PostNotFoundException;
import com.davidng.app.repository.CommentRepository;
import com.davidng.app.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public CommentResp createComment(CommentCreateReq req) {
        Long postId = req.getPostId();

        Optional<Post> optionalPost = postRepository.findById(req.getPostId());

        if(optionalPost.isEmpty())
            throw new PostNotFoundException("Comment should belong on a post, but post not found with id " + postId);

        Comment comment = new Comment();
        comment.setContent(req.getContent());
        comment.setPost(optionalPost.get());

        comment = commentRepository.save(comment);

        log.info("Create new post successfully!");
        log.info("Create new post successfully!");

        return CommentResp.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .build();
    }
}

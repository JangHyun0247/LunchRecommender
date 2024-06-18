package com.sparta.lunchrecommender.domain.comment.service;

import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.comment.dto.CommentResponseDto;
import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.comment.repository.CommentRepository;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.repository.PostRepository;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Post post;
    private Comment comment1;
    private Comment comment2;
    private CommentRequestDto commentRequestDto1;
    private CommentRequestDto commentRequestDto2;

    @BeforeEach
    void setUp() {
        user = new User(1L);
        post = new Post(1L);
        commentRequestDto1 = new CommentRequestDto("댓글 작성1");
        comment1 = new Comment(1L,commentRequestDto1,post,user);
        commentRequestDto2 = new CommentRequestDto("댓글 작성2");
        comment2 = new Comment(2L,commentRequestDto2,post,user);
    }

    @Test
    @DisplayName("댓글 작성")
    void addComment_Success() {
        //given
        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment1);

        //when
        CommentResponseDto comment = commentService.addComment(post.getPostId(), commentRequestDto1, user);

        //then
        assertNotNull(comment);
        assertEquals("댓글 작성1",comment.getContent());
    }

    @Test
    @DisplayName("댓글 찾아오기")
    void findCommentById_Success() {

        //given
        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.findById(comment2.getCommentId())).thenReturn(Optional.of(comment2));

        //when
        CommentResponseDto responseDto = commentService.findCommentById(post.getPostId(), comment2.getCommentId());

        //then
        assertNotNull(responseDto);
        assertEquals(1L, post.getPostId());
        assertEquals(2L, comment2.getCommentId());
        assertEquals("댓글 작성2", comment2.getContent());
    }

    @Test
    @DisplayName("게시물의 모든 댓글 찾아오기")
    void findCommentAll_Success() {
        //given
        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.findAllByPostId(post.getPostId())).thenReturn(Arrays.asList(comment1,comment2));

        //when
        List<CommentResponseDto> comments = commentService.findCommentAll(post.getPostId());

        //then
        assertNotNull(comments);
        assertEquals("댓글 작성1",comments.get(0).getContent());
        assertEquals("댓글 작성2",comments.get(1).getContent());
        assertEquals(2, comments.size());
    }

    @Test
    @DisplayName("1번 댓글 수정하기")
    void updateComment_Success() {
        //given
        CommentRequestDto commentUpdateRes = new CommentRequestDto("댓글 수정하기");
        when(commentRepository.findById(comment1.getCommentId())).thenReturn(Optional.of(comment1));

        //when
        CommentResponseDto comment = commentService.updateComment(post.getPostId(), comment1.getCommentId(), commentUpdateRes, user);

        //then
        assertNotNull(comment);
        assertEquals("댓글 수정하기", comment.getContent());
    }

    @Test
    @DisplayName("1번 댓글 수정하기 - 사용자가 아닌 경우")
    void updateComment_NotAuthor() {
        //given
        CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 작성");
        User otherUser = new User(2L);
        when(commentRepository.findById(comment1.getCommentId())).thenReturn(Optional.of(comment1));

        //when - then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(post.getPostId(), comment1.getCommentId(), commentRequestDto, otherUser);
        });

        assertEquals("댓글 작성자만 수정할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("2번 댓글 삭제하기")
    void deleteComment_Success() {
        //given
        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment2);
        when(commentRepository.findById(comment2.getCommentId())).thenReturn(Optional.of(comment2))
                .thenReturn(Optional.empty());

        //when
        commentService.addComment(post.getPostId(), commentRequestDto2, user);
        commentService.deleteComment(post.getPostId(), comment2.getCommentId(), user);

        //then
        assertTrue(commentRepository.findById(comment2.getCommentId()).isEmpty());

    }

    @Test
    @DisplayName("2번 댓글 삭제하기 - 사용자가 아닌 경우")
    void deleteComment_NotAuthor() {
        //given
        User otherUser = new User(2L);
        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment2);
        when(commentRepository.findById(comment2.getCommentId())).thenReturn(Optional.of(comment2))
                .thenReturn(Optional.empty());

        //when - then
        commentService.addComment(post.getPostId(), commentRequestDto2, user);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(post.getPostId(), comment2.getCommentId(), otherUser);
        });

        assertEquals("댓글 작성자만 삭제할 수 있습니다.", exception.getMessage());
        verify(commentRepository, times(1)).findById(comment2.getCommentId());
        verify(commentRepository, times(0)).delete(any(Comment.class));
    }
}

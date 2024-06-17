package com.sparta.lunchrecommender.mvc.service;

import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.repository.PostRepository;
import com.sparta.lunchrecommender.domain.post.service.PostService;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.user.repository.UserRepository;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    PostService postService;

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Post post1;
    private Post post2;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        startDate = LocalDateTime.of(2024, 6, 1, 0, 0);
        endDate = LocalDateTime.of(2024, 6, 30, 23, 59);
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

        User user1 = new User(
                "wkdgus1111",
                "Wkdgus1004ok!",
                "주장현",
                "주장현닉네임",
                "yugi@naver.com",
                "한줄 소개", UserStatus.ACTIVE);

        User user2 = new User(
                "wkdgus1111",
                "Wkdgus1004ok!",
                "주장현",
                "주장현닉네임",
                "yugi@naver.com",
                "한줄 소개", UserStatus.ACTIVE);

        post1 = new Post(new PostCreateRequestDto("게시물 내용1"));
        post1.setUser(user1);
        post2 = new Post(new PostCreateRequestDto("게시물 내용2"));
        post2.setUser(user2);
    }

    @Test
    @DisplayName("게시물 목록 조회 - 기간 지정")
    void testGetPostsBetweenDates() {
        // given
        List<Post> mockPosts = Arrays.asList(post1, post2);
        Page<Post> postPage = new PageImpl<>(mockPosts, pageable, mockPosts.size());

        given(postRepository.findAllByCreatedAtBetween(startDate, endDate, pageable)).willReturn(postPage);

        // when
        Page<PostResponseDto> result = postService.getPosts(0, "createdAt", startDate, endDate);

        // then
        assertEquals("게시물 내용1", result.getContent().get(0).getContent());
        assertEquals("게시물 내용2", result.getContent().get(1).getContent());
    }

    @Test
    @DisplayName("게시물 작성")
    void createPost() {
        // given
        PostCreateRequestDto requestDto = new PostCreateRequestDto("새로운 게시물 내용");
        User user = new User();
        Post newPost = new Post(requestDto);
        newPost.setUser(user);

        given(postRepository.save(any(Post.class))).willReturn(newPost);

        // when
        postService.createPost(requestDto, user);

        // then
        assertNotNull(newPost);
        assertEquals("새로운 게시물 내용", newPost.getContent());

    }

    @Test
    @DisplayName("게시물 수정 테스트 - 성공")
    void updatePost_Success() {
        // given
        Long postId = 1L;
        //수정 할 requestDto
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("게시물 수정");
        //유저 생성
        User user = new User(1L);

        //게시물 생성
        Post post = new Post(new PostCreateRequestDto("게시물 생성"));
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.updatePost(postId, requestDto, user);

        // then
        assertNotNull(post);
        assertEquals("게시물 수정", post.getContent());
        assertEquals(user, post.getUser());
    }

    @Test
    @DisplayName("게시물 수정 테스트 - 실패 (사용자가 작성자가 아님)")
    void updatePost_Failure_NotAuthor() {
        // given
        Long postId = 1L;
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto("게시물 수정");

        User user = new User(1L);
        User differentUser = new User(2L);

        //2번 유저로 set
        Post post = new Post(new PostCreateRequestDto("게시물 작성"));
        post.setUser(differentUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.updatePost(postId, requestDto, user);
        });
    }

    @Test
    @DisplayName("게시물 삭제 테스트 - 성공")
    void deletePost_Success() {
        // given
        Long postId = 1L;
        User user = new User(1L);
        Post post = new Post(new PostCreateRequestDto("게시물 작성"));
        post.setUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when
        postService.deletePost(postId, user);

        // then
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시물 삭제 테스트 - 실패 (사용자가 작성자가 아님)")
    void deletePost_Failure_NotAuthor() {
        // given
        Long postId = 1L;
        User user = new User(1L);
        User differentUser = new User(2L);
        Post post = new Post(new PostCreateRequestDto("게시물 작성"));
        post.setUser(differentUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            postService.deletePost(postId, user);
        });

        verify(postRepository, times(0)).delete(any(Post.class));
    }
}
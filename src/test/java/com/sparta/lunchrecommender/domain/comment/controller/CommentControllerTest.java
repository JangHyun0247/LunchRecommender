package com.sparta.lunchrecommender.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.comment.dto.CommentResponseDto;
import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.comment.service.CommentService;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.global.config.WebSecurityConfig;
import com.sparta.lunchrecommender.global.security.UserDetailsImpl;
import com.sparta.lunchrecommender.global.security.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        //테스트 할 컨트롤러
        controllers = {CommentController.class},
        //제외할 필터
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class CommentControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
    }

    //Mock 테스트 할 가짜 유저
    private void mockUserSetup() {
        String loginId = "wkdgus1111";
        String password = "Wkdgus1004ok!";
        String name = "주장현";
        String nickname = "주장현닉네임";
        String email = "yugi82828@naver.com";
        String intro = "한줄 소개";
        UserStatus status = UserStatus.ACTIVE;
        User testUser = new User(loginId, password, name, nickname, email, intro, status);
        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        //가짜 인증을 위한
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    //게시물
    PostCreateRequestDto requestDto = new PostCreateRequestDto("게시물 작성");
    private final Post post = new Post(requestDto);

    //유저
    private final User user = new User(
            "wkdgus1111",
            "Wkdgus1004ok!",
            "주장현",
            "주장현닉네임",
            "yugi@naver.com",
            "한줄 소개", UserStatus.ACTIVE);

    //댓글
    CommentRequestDto requestDto1 = new CommentRequestDto("댓글 작성1");
    CommentRequestDto requestDto2 = new CommentRequestDto("댓글 작성2");
    Comment comment1 = new Comment(requestDto1,post,user);
    Comment comment2 = new Comment(requestDto2,post,user);

    @Test
    @DisplayName("댓글 작성")
    void addComment() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;
        String content = "댓글 작성";
        CommentRequestDto comment = new CommentRequestDto(content);
        String addComment = objectMapper.writeValueAsString(comment);

        //when - then
        mvc.perform(post("/api/post/{post_id}/comment", post_id)
                        .content(addComment)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 조회")
    void findCommentById() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;
        Long comment_id = 2L;

        //when - then
        mvc.perform(get("/api/post/{post_id}/comment/{comment_id}", post_id, comment_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("댓글 조회가 완료되었습니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("모든 댓글 조회")
    void findCommentAll() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;

        List<CommentResponseDto> comments = new ArrayList<>(Arrays.asList(new CommentResponseDto(comment1), new CommentResponseDto(comment2)));
        when(commentService.findCommentAll(eq(post_id))).thenReturn(comments);

        //when - then
        mvc.perform(get("/api/post/{post_id}/comment/getList", post_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물의 모든 댓글 조회 완료되었습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("빈 댓글 조회")
    void findEmptyCommentAll() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;

        List<CommentResponseDto> comments = new ArrayList<>(Collections.emptyList());
        when(commentService.findCommentAll(eq(post_id))).thenReturn(comments);

        //when - then
        mvc.perform(get("/api/post/{post_id}/comment/getList", post_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("가장 먼저 댓글을 남겨보세요!"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;
        Long comment_id = 1L;
        String content = "댓글 내용 업데이트";
        CommentRequestDto requestDto = new CommentRequestDto(content);
        String updateInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(patch("/api/post/{post_id}/comment/{comment_id}", post_id, comment_id)
                        .content(updateInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;
        Long comment_id = 1L;

        //when - then
        mvc.perform(delete("/api/post/{post_id}/comment/{comment_id}", post_id, comment_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
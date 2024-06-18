package com.sparta.lunchrecommender.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.service.PostService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        //테스트 할 컨트롤러
        controllers = {PostController.class},
        //제외할 필터
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class PostControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    PostCreateRequestDto requestDto = new PostCreateRequestDto("게시물 작성");
    private final Post post = new Post(requestDto);

    private final User user = new User(
            "wkdgus1111",
            "Wkdgus1004ok!",
            "주장현",
            "주장현닉네임",
            "yugi@naver.com",
            "한줄 소개", UserStatus.ACTIVE);

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

    @Test
    @DisplayName("게시물 작성")
    void createPost() throws Exception {
        //given
        this.mockUserSetup();
        String content = "게시물 작성";
        PostCreateRequestDto requestDto = new PostCreateRequestDto(content);
        String postInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(post("/api/post")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 업데이트")
    void updatePost() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;
        String content = "게시물 내용 업데이트";
        PostUpdateRequestDto requestDto = new PostUpdateRequestDto(content);
        String updateInfo = objectMapper.writeValueAsString(requestDto);

        //when - then
        mvc.perform(patch("/api/post/{post_id}", post_id)
                        .content(updateInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() throws Exception {
        //given
        this.mockUserSetup();
        Long post_id = 1L;

        //when - then
        mvc.perform(delete("/api/post/{post_id}", post_id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시물 조회")
    public void getPosts() throws Exception {
        // given
        this.mockUserSetup();
        MultiValueMap<String, String> listRequestForm = new LinkedMultiValueMap<>();
        listRequestForm.add("page", "0");
        listRequestForm.add("sortBy", "createdAt");
        listRequestForm.add("startDate", "20240613");
        listRequestForm.add("endDate", "20240613");

        Page<PostResponseDto> posts = new PageImpl<>(Collections.singletonList(new PostResponseDto(post, user)));
        given(postService.getPosts(eq(0), eq("createdAt"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .willReturn(posts);

        // when - then
        mvc.perform(get("/api/posts/getList")
                        .params(listRequestForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시물 조회가 완료 되었습니다."));
    }

    @Test
    @DisplayName("빈 게시물 조회")
    public void getEmptyPosts() throws Exception {
        // given
        this.mockUserSetup();
        MultiValueMap<String, String> listRequestForm = new LinkedMultiValueMap<>();
        listRequestForm.add("page", "0");
        listRequestForm.add("sortBy", "createdAt");
        listRequestForm.add("startDate", "20240613");
        listRequestForm.add("endDate", "20240613");

        Page<PostResponseDto> posts = new PageImpl<>(Collections.emptyList());
        given(postService.getPosts(eq(0), eq("createdAt"), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(posts);

        // when - then
        mvc.perform(get("/api/posts/getList")
                        .params(listRequestForm)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("먼저 작성하여 소식을 알려보세요!"))
                .andDo(print());
    }
}
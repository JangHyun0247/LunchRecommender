package com.sparta.lunchrecommender.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lunchrecommender.domain.post.controller.PostController;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostResponseDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.post.service.PostService;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.global.config.WebSecurityConfig;
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
import org.springframework.security.test.context.support.WithMockUser;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {PostController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class test {

    @MockBean
    PostService postService;

    @Autowired
    private MockMvc mockMvc;

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .alwaysDo(print())
                .build();
    }

    @Test
    @DisplayName("게시물 조회")
    @WithMockUser(username = "wkdgus1111", roles = {"USER"})
    public void getPosts() throws Exception {
        // given
//        this.mockUserSetup();
        MultiValueMap<String, String> listRequestForm = new LinkedMultiValueMap<>();
        listRequestForm.add("page", "0");
        listRequestForm.add("sortBy", "createdAt");
        listRequestForm.add("startDate", "20240613");
        listRequestForm.add("endDate", "20240613");

        PostCreateRequestDto requestDto = new PostCreateRequestDto("게시물 작성");
        Post post = new Post(requestDto);

        User user = new User(
                "wkdgus1111",
                "Wkdgus1004ok!",
                "주장현",
                "주장현닉네임",
                "yugi@naver.com",
                "한줄 소개", UserStatus.ACTIVE);

        Page<PostResponseDto> posts = new PageImpl<>(Collections.singletonList(new PostResponseDto(post, user)));
        given(postService.getPosts(eq(0), eq("createdAt"), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(posts);

        // when - then
        mockMvc.perform(get("/api/posts/getList")
//                        .params(listRequestForm)
                        .contentType(MediaType.APPLICATION_JSON))
//                        .principal(mockPrincipal))
                .andExpect(status().isOk());
    }
}

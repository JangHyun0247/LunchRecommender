package com.sparta.lunchrecommender.domain.post.entity;

import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("게시물 엔티티 테스트")
class PostTest {

    PostCreateRequestDto postCreate = new PostCreateRequestDto("게시물 내용");
    private final Post post = new Post(postCreate);

    @Test
    @DisplayName("게시물 업데이트 테스트")
    void update() {

        //given
        PostUpdateRequestDto postUpdate = new PostUpdateRequestDto("게시물 내용 수정");

        //when
        post.update(postUpdate);

        //then
        assertNotEquals("게시물 내용",post.getContent());
        assertEquals("게시물 내용 수정",post.getContent());

    }
}
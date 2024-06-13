package com.sparta.lunchrecommender.entity;

import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import com.sparta.lunchrecommender.domain.comment.entity.Comment;
import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.entity.Post;
import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Nested
@DisplayName("댓글 엔티티 테스트")
class CommentTest {
    //게시물 생성
    PostCreateRequestDto postCreate = new PostCreateRequestDto("게시물 내용");
    private final Post post = new Post(postCreate);

    //user 생성
    private final User userProfile = new User(
            "wkdgus1111",
            "Wkdgus1004ok!",
            "주장현",
            "주장현닉네임",
            "yugi@naver.com",
            "한줄 소개", UserStatus.ACTIVE);

    //댓글 생성
    CommentRequestDto commentCreate = new CommentRequestDto("댓글 작성");
    private final Comment comment = new Comment(commentCreate, post, userProfile);

    @Test
    @DisplayName("댓글 업데이트 테스트")
    void update() {

        //given
        CommentRequestDto commentUpdate = new CommentRequestDto("댓글 내용 수정");

        //when
        comment.update(commentUpdate);

        //then
        assertNotEquals("댓글 작성", comment.getContent());
        assertEquals("댓글 내용 수정", comment.getContent());
    }
}
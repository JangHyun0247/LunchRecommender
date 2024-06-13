package com.sparta.lunchrecommender.entity;

import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.dto.ProfileRequestDto;
import com.sparta.lunchrecommender.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Nested
@DisplayName("user 메서드 테스트")
class UserTest {

private final User user = new User(
            "wkdgus1111",
            "Wkdgus1004ok!",
            "주장현",
            "주장현닉네임",
            "yugi@naver.com",
            "한줄 소개", UserStatus.ACTIVE);

    @Test
    @DisplayName("프로필 수정 테스트")
    void update() {
        //given
        ProfileRequestDto profileRequestDto = new ProfileRequestDto("이진욱","진욱님","한 줄 소개 프로필","wkdgus1111@naver.com","Wkdgus1004ok!");

        //when
        user.update(profileRequestDto);

        //then
        assertNotEquals("주장현닉네임",user.getNickname());
        assertEquals("wkdgus1111@naver.com",user.getEmail());
    }

    @Test
    @DisplayName("상태 변경 테스트")
    void setStatus() {
        //given
        UserStatus userStatus = UserStatus.DELETED;

        //when
        user.setStatus(userStatus);

        //then
        assertNotEquals("정상",user.getStatus());
        assertEquals("탈퇴",user.getStatus());
    }

}
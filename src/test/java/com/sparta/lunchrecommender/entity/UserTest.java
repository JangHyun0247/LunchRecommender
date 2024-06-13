package com.sparta.lunchrecommender.entity;

import com.sparta.lunchrecommender.domain.user.constant.UserStatus;
import com.sparta.lunchrecommender.domain.user.entity.User;
import com.sparta.lunchrecommender.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("user 엔티티 저장 테스트")
    void test1(){
        //given
        User user = new User(
                "wkdgus1111",
                "Wkdgus1004ok!",
                "주장현",
                "주장현닉네임",
                "wkdgus1111@naver.com",
                "한줄 소개", UserStatus.ACTIVE);

        //when
        userRepository.save(user);


    }
}
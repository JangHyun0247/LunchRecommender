package com.sparta.lunchrecommender.dto;

import com.sparta.lunchrecommender.domain.user.dto.UserRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;
import java.util.Set;

class UserRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll //테스트 하기 전 초기 설정, static 메서드여야 함
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory(); //Validator 객체를 얻기 위해 ValidatorFactory 생성
        validator = factory.getValidator(); //Validator 객체 생성
    }

    @Nested
    @DisplayName("아이디 테스트")
    class IdTest {

        @Test
        @DisplayName("아이디 길이 성공 테스트")
        void test1() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("아이디는 최소 10자 이상 최대 20자 이하입니다.", violation.getMessage());
            }
        }

        @Test
        @DisplayName("아이디 길이 실패 테스트")
        void test2() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("아이디는 최소 10자 이상 최대 20자 이하입니다.", violation.getMessage());
            }
        }

    }

    //비밀번호 특수기호, 대문자 생략
    @Nested
    @DisplayName("패스워드 테스트")
    class PasswordTest {

        @Test
        @DisplayName("비밀번호 형식 성공 테스트")
        void test1() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("비밀번호는 대문자, 소문자, 숫자, 특수 기호를 하나씩 포함해야 하며, 최소 10자 이상이어야 합니다.", violation.getMessage());
            }
        }

        @Test
        @DisplayName("비밀번호 형식 실패 테스트")
        void test2() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("비밀번호는 대문자, 소문자, 숫자, 특수 기호를 하나씩 포함해야 하며, 최소 10자 이상이어야 합니다.", violation.getMessage());
            }
        }

    }

    @Nested
    @DisplayName("이름 테스트")
    class NameTest {

        @Test
        @DisplayName("이름 빈값 성공 테스트")
        void test1() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("필수 입력 값입니다.", violation.getMessage());
            }
        }

        @Test
        @DisplayName("이름 빈값 실패 테스트")
        void test2() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("필수 입력 값입니다.", violation.getMessage());
            }
        }

    }

    @Nested
    @DisplayName("닉네임 테스트")
    class NickNameTest {

        @Test
        @DisplayName("닉네임 빈값 성공 테스트")
        void test1() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("필수 입력 값입니다.", violation.getMessage());
            }
        }

        @Test
        @DisplayName("닉네임 빈값 실패 테스트")
        void test2() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("필수 입력 값입니다.", violation.getMessage());
            }
        }

    }

    @Nested
    @DisplayName("이메일 테스트")
    class EmailTest {

        @Test
        @DisplayName("이메일 형식 성공 테스트")
        void test1() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828@naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("유효한 이메일 형식이어야 합니다.", violation.getMessage());
            }
        }

        @Test
        @DisplayName("이메일 형식 실패 테스트")
        void test2() {

            //given
            UserRequestDto userRequestDto = new UserRequestDto(
                    "wkdgus1111",
                    "Wkdgus1004ok!",
                    "주장현",
                    "주장현닉네임",
                    "yugi82828naver.com",
                    "한줄 소개");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);

            //then
            for (ConstraintViolation<UserRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("유효한 이메일 형식이어야 합니다.", violation.getMessage());
            }
        }

    }

}
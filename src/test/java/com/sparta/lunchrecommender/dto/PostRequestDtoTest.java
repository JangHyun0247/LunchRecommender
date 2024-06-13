package com.sparta.lunchrecommender.dto;

import com.sparta.lunchrecommender.domain.post.dto.PostCreateRequestDto;
import com.sparta.lunchrecommender.domain.post.dto.PostUpdateRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

class PostRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll //테스트 하기 전 초기 설정, static 메서드여야 함
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory(); //Validator 객체를 얻기 위해 ValidatorFactory 생성
        validator = factory.getValidator(); //Validator 객체 생성
    }

    @Nested
    @DisplayName("게시물 업데이트 테스트")
    class UpdateTest {

        @Test
        @DisplayName("게시물 내용 빈값 성공 테스트")
        void test1() {

            //given
            PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto("게시물 수정하기");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(postUpdateRequestDto);

            //then
            for (ConstraintViolation<PostUpdateRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("수정 할 내용을 입력해주세요!", violation.getMessage());
            }
        }

        @Test
        @DisplayName("게시물 내용 빈값 실패 테스트")
        void test2() {

            //given
            PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto("");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<PostUpdateRequestDto>> violations = validator.validate(postUpdateRequestDto);

            //then
            for (ConstraintViolation<PostUpdateRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("수정 할 내용을 입력해주세요!", violation.getMessage());
            }
        }

    }

    @Nested
    @DisplayName("게시물 작성 테스트")
    class CreateTest {

        @Test
        @DisplayName("게시물 내용 빈값 성공 테스트")
        void test1() {

            //given
            PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto("게시물 수정하기");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<PostCreateRequestDto>> violations = validator.validate(postCreateRequestDto);

            //then
            for (ConstraintViolation<PostCreateRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("작성할 내용을 입력해주세요!", violation.getMessage());
            }
        }

        @Test
        @DisplayName("게시물 내용 빈값 실패 테스트")
        void test2() {

            //given
            PostCreateRequestDto postCreateRequestDto = new PostCreateRequestDto("");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<PostCreateRequestDto>> violations = validator.validate(postCreateRequestDto);

            //then
            for (ConstraintViolation<PostCreateRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("작성할 내용을 입력해주세요!", violation.getMessage());
            }
        }

    }
}
package com.sparta.lunchrecommender.dto;

import com.sparta.lunchrecommender.domain.comment.dto.CommentRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

class CommentRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll //테스트 하기 전 초기 설정, static 메서드여야 함
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory(); //Validator 객체를 얻기 위해 ValidatorFactory 생성
        validator = factory.getValidator(); //Validator 객체 생성
    }

    @Nested
    @DisplayName("댓글 작성 테스트")
    class UpdateTest {

        @Test
        @DisplayName("댓글 내용 빈값 성공 테스트")
        void test1() {

            //given
            CommentRequestDto commentRequestDto = new CommentRequestDto("댓글 작성하기");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

            //then
            Assertions.assertTrue(violations.isEmpty(),"필수 입력 값입니다. 유효성 검사에 실패했습니다.");
        }

        @Test
        @DisplayName("댓글 내용 빈값 실패 테스트")
        void test2() {

            //given
            CommentRequestDto commentRequestDto = new CommentRequestDto("");

            //when
            //validator 객체를 사용하여 검사
            //검사 결과 ConstraintViolation 객체의 Set
            Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(commentRequestDto);

            //then
            for (ConstraintViolation<CommentRequestDto> violation : violations) {
                //검사에서 발생한 모든 에러 출력
                Assertions.assertEquals("댓글 내용을 입력해주세요!", violation.getMessage());
            }
        }

    }

}
package com.sparta.lunchrecommender.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank(message = "댓글 내용을 입력해주세요!")
    private String content;

    @JsonCreator // 필드가 1개라서 deserialize 에러 발생 -> 해결
    public CommentRequestDto(String content) {
        this.content = content;
    }
}

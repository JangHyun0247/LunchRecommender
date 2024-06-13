package com.sparta.lunchrecommender.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostUpdateRequestDto {

    @NotBlank(message = "수정 할 내용을 입력해주세요!")
    private String content;

    @JsonCreator // 필드가 1개라서 deserialize 에러 발생 -> 해결
    public PostUpdateRequestDto(String content) {
        this.content=content;
    }
}

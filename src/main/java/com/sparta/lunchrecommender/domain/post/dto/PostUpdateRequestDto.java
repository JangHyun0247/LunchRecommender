package com.sparta.lunchrecommender.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostUpdateRequestDto {
    @NotBlank(message = "수정 할 내용을 입력해주세요!")
    private String content;
}

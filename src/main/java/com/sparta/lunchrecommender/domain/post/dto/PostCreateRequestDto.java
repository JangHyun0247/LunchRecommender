package com.sparta.lunchrecommender.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequestDto {
    @NotBlank(message = "작성할 내용을 입력해주세요!")
    private String content;
}

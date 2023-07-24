package com.example.knu.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class BoardCategoriesResponseDto {

    private List<BoardCategoriesDto> list;

    @AllArgsConstructor
    @Data
    public static class BoardCategoriesDto {
        private Long id;
        private String name;
    }
}

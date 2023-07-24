package com.example.knu.dto.board.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class BoardResponseDto {

    private List<BoardDto> list;

    @AllArgsConstructor
    @Data
    public static class BoardDto {
        private Long id;
        private String name;
        private String description;
    }
}

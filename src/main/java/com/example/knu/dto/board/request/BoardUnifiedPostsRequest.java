package com.example.knu.dto.board.request;

import com.example.knu.dto.board.request.sort.BoardUnifiedPostsSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardUnifiedPostsRequest {

    // 필터
    private String input;

    private Long categoryId;

    // 페이징
    private Integer pageNumber = 1;

    private Integer pageSize = 10;

    private BoardUnifiedPostsSort sort = BoardUnifiedPostsSort.UPDATE_AT_DESC;
}

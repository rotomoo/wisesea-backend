package com.example.knu.dto.board.response;

import com.example.knu.common.PagingResponse;
import com.example.knu.domain.mapping.BoardUnifiedPostMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class BoardUnifiedPostsResponse implements Serializable {

    private PagingResponse pagingResponse;

    private List<BoardUnifiedPostMapping> list;
}

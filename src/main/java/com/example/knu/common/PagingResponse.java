package com.example.knu.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PagingResponse {

    private Long totalCount = 0l;

    private Integer totalPageCount = 0;

    private Integer pageNumber = 1;

    private Integer pageSize = 10;

    public static PagingResponse createPagingInfo(Page<?> pageData) {
        return new PagingResponse(pageData.getTotalElements(), pageData.getTotalPages(),
                pageData.getNumber() + 1,  pageData.getSize());
    }
}

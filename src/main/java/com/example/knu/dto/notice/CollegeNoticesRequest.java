package com.example.knu.dto.notice;

import com.example.knu.dto.notice.sort.CollegeNoticesSort;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollegeNoticesRequest {

    // 페이징
    private Integer pageNumber = 1;

    private Integer pageSize = 10;

    private CollegeNoticesSort sort = CollegeNoticesSort.DATE_DESC;
}

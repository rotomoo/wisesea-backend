package com.example.knu.dto.notice;

import com.example.knu.common.PagingResponse;
import com.example.knu.domain.mapping.CollegeNoticesMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class CollegeNoticesResponse implements Serializable {

    private PagingResponse pagingResponse;

    private List<CollegeNoticesMapping> list;
}

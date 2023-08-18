package com.example.knu.domain.mapping;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CollegeNoticesMapping {

    private Long id;
    private String code;
    private String title;
    private String write;
    private String date;
    private int view;
    private String url;
}

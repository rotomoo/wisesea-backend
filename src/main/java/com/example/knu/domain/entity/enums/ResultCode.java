package com.example.knu.domain.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(HttpStatus.OK);
    private final HttpStatus httpStatus;

}

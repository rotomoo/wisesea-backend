package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.exception.DuplicateMemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

    @GetMapping("/api/get/test/v1")
    public ResponseEntity<?> getTestV1() {

        return ResponseEntity.ok("test");
    }

    @GetMapping("/api/get/test/v2")
    public Response<?> getTestV2() {

        return Response.success("test2");
    }

    @GetMapping("/api/get/test/v3")
    public Response<?> getTestV3() {

        if (true) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        return Response.success("test3");
    }
}

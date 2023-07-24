package com.example.knu.controller;

import com.example.knu.common.Response;
import com.example.knu.domain.entity.user.JwtToken;
import com.example.knu.dto.notice.NoticeCreation;
import com.example.knu.dto.user.ProfileImageDto;
import com.example.knu.dto.user.ReissueRequest;
import com.example.knu.dto.user.UserDto;
import com.example.knu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/api/user");
    }

    @PostMapping("/signup")
    public Response signup(@Valid @RequestBody UserDto userDto) {
        return Response.success(userService.signup(userDto));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody Map<String, String> loginForm){
        JwtToken token = userService.login(loginForm.get("username"), loginForm.get("password"));
        return ResponseEntity.ok(token);
    }





    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }

    /**
     * 리프레시 토큰 갱신
     */
    @PostMapping("/all/refresh-token")
    public Response reissue(HttpServletRequest request, @RequestBody @Valid ReissueRequest reissueRequest) {

        Response response = userService.reissue(request, reissueRequest);

        return response;
    }

    /**
     * 프로필 조회
     */
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/user/profile")
    public Response getProfile(Principal principal) {

        Response response = userService.getProfile(principal);

        return response;
    }

    /**
     * 프로필 이미지 변경
     */
    @PreAuthorize("hasAnyRole('USER')")
    @PatchMapping("/user/profile/image")
    public Response updateProfileImage(Principal principal,
                               @ModelAttribute @Valid ProfileImageDto profileImageDto) throws IOException {

        Response response = userService.updateProfileImage(principal, profileImageDto);

        return response;
    }
}
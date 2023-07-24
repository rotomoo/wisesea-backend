package com.example.knu.controller;

import com.example.knu.dto.profile.ProfileUsernameUpdateDto;
import com.example.knu.dto.profile.UserPasswordUpdateDto;
import com.example.knu.dto.profile.UserResponseDto;
import com.example.knu.service.profile.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profile")
public class ProfileController {



    private final ProfileService profileService;
    /**
     * 회원 목록 조회
     * @param model
     * @return 회원 목록 페이지
     */
    @GetMapping("/list")
    public String users(Model model) {
        List<UserResponseDto> users = profileService.findUsers();
        model.addAttribute("users", users);

        return "/profile/userList";
    }

    /**
     * 회원 정보 조회
     * @param model
     * @param authentication 인증 정보
     * @return 회원 정보 페이지
     */
    @GetMapping("/info")
    public String memberInfo(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDto user = profileService.findUser(userDetails.getUsername());


        model.addAttribute("user", user);

        return "/profile/info";
    }

    /**
     * 회원 이름 변경
     * @param model
     * @param authentication 인증 정보
     * @return 회원 이름 변경 페이지
     */
    @GetMapping("/update/username")
    public String updateUsernameForm(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserResponseDto user = profileService.findUser(userDetails.getUsername());
        model.addAttribute("user", user);

        return "/profile/updateUsername";
    }

    /**
     * 회원 이름 변경 post
     * @param profileUsernameUpdateDTO
     * @param errors
     * @param model
     * @return 회원 정보 페이지
     */
    @PatchMapping("/update/username")
    public String updateUsername(@Valid ProfileUsernameUpdateDto profileUsernameUpdateDTO, Errors errors, Model model, Authentication authentication) {
        if (errors.hasErrors()) {
            model.addAttribute("user", profileUsernameUpdateDTO);

            return "/profile/updateUsername";
        }

        profileService.updateProfileUsername(profileUsernameUpdateDTO);

        return "redirect:/profile/info";
    }

    /**
     * 회원 비밀번호 변경
     * @return 회원 비밀번호 변경 페이지
     */
    @GetMapping("/update/password")
    public String updatePasswordForm() {
        return "/profile/updatePassword";
    }
    /**
     * 회원 비밀번호 변경 post
     * @param userPasswordUpdateDto
     * @param model
     * @param authentication
     * @return 회원 정보 페이지
     */
    @PatchMapping("/update/password")
    public String updatePassword(@Valid UserPasswordUpdateDto userPasswordUpdateDto, Model model, Authentication authentication) {
        // new password 비교
        if (!Objects.equals(userPasswordUpdateDto.getNewPassword(), userPasswordUpdateDto.getConfirmPassword())) {
            model.addAttribute("dto", userPasswordUpdateDto);
            model.addAttribute("differentPassword", "비밀번호가 같지 않습니다.");
            return "/profile/updatePassword";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long result = profileService.updateProfilePassword(userPasswordUpdateDto, userDetails.getUsername());

        // 현재 비밀번호가 틀렸을 경우
        if (result == null) {
            model.addAttribute("dto", userPasswordUpdateDto);
            model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
            return "/profile/updatePassword";
        }

        return "redirect:/profile/info";
    }

    /**
     * 회원 탈퇴
     * @return 회원 탈퇴 페이지
     */
    @GetMapping("/withdrawal")
    public String memberWithdrawalForm() {
        return "/profile/withdrawal";
    }

    /**
     * 회원 탈퇴 post
     * @param password
     * @param model
     * @param authentication
     * @return 홈 페이지
     */
    @PostMapping("/withdrawal")
    public String memberWithdrawal(@RequestParam String password, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean result = profileService.withdrawal(userDetails.getUsername(), password);

        if (result) {
            return "redirect:/logout";
        } else {
            model.addAttribute("wrongPassword", "비밀번호가 맞지 않습니다.");
            return "/profile/withdrawal";
        }
    }
}

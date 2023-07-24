package com.example.knu.service.profile;

import com.example.knu.dto.profile.ProfileUsernameUpdateDto;
import com.example.knu.dto.profile.UserPasswordUpdateDto;
import com.example.knu.dto.profile.UserPasswordUpdateDto;
import com.example.knu.dto.profile.UserResponseDto;

import java.util.List;

public interface ProfileService {
    /**
     * 회원 목록 조회
     * @return 회원 정보 목록
     */
    List<UserResponseDto> findUsers();

    /**
     * 회원 정보 조회
     * @return 회원 정보
     */
    UserResponseDto findUser(String username);

    /**
     * 회원 이름 변경
     * @param profileUsernameUpdateDto
     * @return 회원 ID
     */
    Long updateProfileUsername(ProfileUsernameUpdateDto profileUsernameUpdateDto);

    /**
     * 회원 비밀번호 변경
     * @param userPasswordUpdateDto
     * @return 회원 ID
     */
    Long updateProfilePassword(UserPasswordUpdateDto userPasswordUpdateDto, String username);

    /**
     * 회원 탈퇴
     * @param username
     * @param password
     * @return boolean
     */
    boolean withdrawal(String username, String password);
}

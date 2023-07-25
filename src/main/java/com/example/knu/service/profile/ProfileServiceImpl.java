package com.example.knu.service.profile;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.ProfileRepository;
import com.example.knu.dto.profile.ProfileUsernameUpdateDto;
import com.example.knu.dto.profile.UserPasswordUpdateDto;
import com.example.knu.dto.profile.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDto> findUsers() {
        List<User> all = profileRepository.findAll();
        List<UserResponseDto> members = new ArrayList<>();

        for (User user: all) {
            UserResponseDto build = UserResponseDto.builder()
                    .user(user)
                    .build();
            members.add(build);
        }

        return members;
    }

    @Override
    public UserResponseDto findUser(String username) {
        User user = profileRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        UserResponseDto result = UserResponseDto.builder()
                .user(user)
                .build();

        return result;
    }

    @Override
    public Long updateProfileUsername(ProfileUsernameUpdateDto profileUsernameUpdateDto) {
//        User user = profileRepository.findByUsername(profileUsernameUpdateDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));
//
//        user.updateUsername(profileUsernameUpdateDto.getUsername());
//        profileRepository.save(user);
//
//        return user.getUserId();
        return null;
    }

    @Override
    public Long updateProfilePassword(UserPasswordUpdateDto userPasswordUpdateDto, String username) {
        User user = profileRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        if (!passwordEncoder.matches(userPasswordUpdateDto.getCurrentPassword(), user.getPassword())) {
            return null;
        } else {
            userPasswordUpdateDto.setNewPassword(passwordEncoder.encode(userPasswordUpdateDto.getNewPassword()));
            user.updatePassword(userPasswordUpdateDto.getNewPassword());
            profileRepository.save(user);
        }

        return user.getUserId();
    }

    @Override
    public boolean withdrawal(String username, String password) {
        User user = profileRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("이메일이 존재하지 않습니다."));

        if (passwordEncoder.matches(password, user.getPassword())) {
            profileRepository.delete(user);
            return true;
        } else {
            return false;
        }
    }
}

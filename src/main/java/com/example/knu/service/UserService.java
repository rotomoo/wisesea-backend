package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.common.s3.S3Directory;
import com.example.knu.common.s3.S3Uploader;
import com.example.knu.domain.entity.user.Authority;
import com.example.knu.domain.entity.user.JwtToken;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.UserRepository;
import com.example.knu.dto.user.*;
import com.example.knu.exception.CommonException;
import com.example.knu.exception.DuplicateMemberException;
import com.example.knu.exception.NotFoundMemberException;
import com.example.knu.jwt.TokenProvider;
import com.example.knu.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final S3Uploader s3Uploader;

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .loginId(userDto.getUsername())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    public JwtToken login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        JwtToken token = TokenProvider.createToken(authentication);

        return token;
    }


    @Transactional(readOnly = true)
    public UserDto getUserWithAuthorities(String username) {
        return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserDto getMyUserWithAuthorities() {
        return UserDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }

    /**
     * 리프레시 토큰 갱신
     * @param request
     * @param reissueRequest
     * @return
     */
    public Response reissue(HttpServletRequest request, ReissueRequest reissueRequest) {

        String accessToken = request.getHeader("Authorization");
        accessToken = StringUtils.hasText(accessToken) && accessToken.startsWith("Bearer ") ?
                accessToken.substring(7) : null;

        String refreshToken = reissueRequest.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new CommonException("잘못된 refresh token입니다. 다시 로그인 해주세요");
        }

        Authentication accessAuthentication = tokenProvider.getAuthentication(accessToken);
        Authentication refreshAuthentication = tokenProvider.getAuthentication(refreshToken);

        if (!accessAuthentication.getName().equals(refreshAuthentication.getName())) {
            throw new CommonException("access, refresh 토큰이 일치하지 않습니다.");
        }

        JwtToken token = tokenProvider.createToken(accessAuthentication);

        return Response.success(token);
    }

    /**
     * 프로필 조회
     * @param principal
     * @return
     */
    public Response getProfile(Principal principal) {
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

        return Response.success(new UserProfileDto(
                user.getUserId(),
                user.getLoginId(),
                user.getUsername(),
                user.getUserType(),
                user.getEmailReceiveYn(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.isActivated()
        ));
    }

    /**
     * 프로필 이미지 변경
     * @param principal
     * @param profileImageDto
     * @return
     */
    @Transactional
    public Response updateProfileImage(Principal principal, ProfileImageDto profileImageDto) throws IOException {
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

        String profileImageUrl = null;

        if (profileImageDto.getProfileImage() != null && !profileImageDto.getProfileImage().isEmpty()) {

            MultipartFile profileImage = profileImageDto.getProfileImage();
            profileImageUrl = s3Uploader.uploadFileToS3(profileImage,
                    S3Directory.USER_PROFILE.getPath() + user.getUserId() + profileImage.getOriginalFilename());
        }

        user.setProfileImageUrl(profileImageUrl);

        return Response.success(null);
    }

    /**
     * 프로필 닉네임 변경
     * @param principal
     * @param nicknameRequest
     * @return
     */
    @Transactional
    public Response updateProfileNickname(Principal principal, NicknameRequest nicknameRequest) {
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();
        String nickname = nicknameRequest.getNickname();

        if (user.getNickname().equals(nickname)) throw new CommonException("이전 닉네임과 동일합니다.");

        Optional<User> foundUserByNickname = userRepository.findByNickname(nickname);

        if (foundUserByNickname.isPresent()) throw new CommonException("해당 닉네임이 사용중입니다.");

        user.updateNickname(nickname);

        return Response.success(null);
    }

    /**
     * 프로필 수정
     * @param principal
     * @param profileUpdateRequest
     * @return
     */
    @Transactional
    public Response updateProfile(Principal principal, ProfileUpdateRequest profileUpdateRequest) {
        Optional<User> loginUser = userRepository.findByLoginId(principal.getName());
        User user = loginUser.get();

        user.updateUser(
                profileUpdateRequest.getUsername(),
                profileUpdateRequest.getUserType(),
                profileUpdateRequest.getEmailReceiveYn()
        );

        return Response.success(null);

    }
}

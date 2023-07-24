package com.example.knu.service;

import com.example.knu.common.Response;
import com.example.knu.domain.entity.user.Authority;
import com.example.knu.domain.entity.user.JwtToken;
import com.example.knu.domain.entity.user.User;
import com.example.knu.domain.repository.UserRepository;
import com.example.knu.dto.user.ReissueRequest;
import com.example.knu.dto.user.UserDto;
import com.example.knu.exception.CommonException;
import com.example.knu.exception.DuplicateMemberException;
import com.example.knu.exception.NotFoundMemberException;
import com.example.knu.jwt.TokenProvider;
import com.example.knu.util.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.Collections;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public UserDto signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
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
}

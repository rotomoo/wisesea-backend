package com.example.knu.jwt;

import com.example.knu.common.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
   @Override
   public void commence(HttpServletRequest request,
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException {
      // 유효한 자격증명을 제공하지 않고 접근하려 할때 401

      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      ObjectMapper objectMapper = new ObjectMapper();
      String msg = objectMapper.writeValueAsString(Response.unauthorized("access token이 만료되었거나 요청 URI가 유효하지 않습니다"));

      log.info("response data : {}", msg);

      response.getWriter().write(msg);
   }
}

package com.sbs.qnaService.base.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
            .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
    ;
    return http.build(); // 기본설정이 적용
  }

  @Bean
  // 비밀번호 암호화 검증
  // 인증 시 암호화 된 비밀번호와 비교
  // BCrypt 암호화 알고리즘으로 비밀번호를 암호화
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}

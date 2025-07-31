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
    // http.formLogin : 스프링 시큐리티가 사용하는 폼 로그인 기능을 활성화

    http.formLogin((formLogin) -> formLogin
        .loginPage("/user/login") // 기본 제공되는 페이지 대신에 login 템플릿을 폼으로 사용
        .defaultSuccessUrl("/")); // 로그인 성공 후 리다이렉트 할 기본 경로

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

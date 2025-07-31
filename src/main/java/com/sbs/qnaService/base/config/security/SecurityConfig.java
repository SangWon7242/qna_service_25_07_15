package com.sbs.qnaService.base.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    // 아래 설정은 로그인시에만 해당 페이지에 접근이 가능하게 설정
    http.authorizeHttpRequests(auth -> {
          auth.requestMatchers("/question/list").permitAll() // 질문 관련 페이지 접근 허용
              .requestMatchers("/question/detail/**").permitAll() // 질문 상세 페이지 접근 허용
              .requestMatchers("/user/signup").permitAll() // 회원가입 페이지 접근 허용
              .requestMatchers("/user/login").permitAll() // 로그인 페이지 접근 허용
              .requestMatchers("/user/logout").permitAll() // 로그아웃 페이지 접근 허용
              .requestMatchers("/style.css").permitAll() // 스타일시트 접근 허용
              .requestMatchers("/").permitAll()
              .anyRequest().authenticated(); // 나머지 요청은 인증 필요
        })
        .formLogin((formLogin) -> formLogin
            .loginPage("/user/login") // 기본 제공되는 페이지 대신에 login 템플릿을 폼으로 사용
            .loginProcessingUrl("/user/login") // 시큐리티한테 로그인 폼 처리 url을 알려줌
            .defaultSuccessUrl("/")) // 로그인 성공 후 리다이렉트 할 기본 경로
        .logout((logout) -> logout
            .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)); // 세션을 무효화

    return http.build(); // 기본설정이 적용
  }

  @Bean
    // 비밀번호 암호화 검증
    // 인증 시 암호화 된 비밀번호와 비교
    // BCrypt 암호화 알고리즘으로 비밀번호를 암호화
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  // AuthenticationManager : 인증을 처리하는 매니저
  // AuthenticationConfiguration : 인증 관련 설정을 제공
  // 로그인시 인증 매니저를 통해 사용자 처리
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}

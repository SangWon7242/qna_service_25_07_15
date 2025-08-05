package com.sbs.qnaService.boudedContext.user.controller;

import com.sbs.qnaService.boudedContext.user.form.UserCreateForm;
import com.sbs.qnaService.boudedContext.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  @GetMapping("/signup")
  public String signup(UserCreateForm userCreateForm) {
    return "signup_form";
  }

  @PostMapping("/signup")
  public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "signup_form";
    }

    if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
      bindingResult.rejectValue("password2", "passwordInCorrect",
          "2개의 패스워드가 일치하지 않습니다.");
      return "signup_form";
    }

    try {
      // 회원가입 처리
      userService.create(userCreateForm.getUsername(),
          userCreateForm.getEmail(), userCreateForm.getPassword1());
    } catch (DataIntegrityViolationException e) {
      // DataIntegrityViolationException : 데이터베이스 무결성 제약 조건 위반
      // 무결성 : 데이터베이스에 저장된 데이터의 정확성과 일관성, 유효성
      e.printStackTrace(); // 예외 정보 출력
      
      // reject : 오류 메시지를 BindingResult에 추가
      bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
      return "signup_form";
    } catch (Exception e) {
      e.printStackTrace();
      bindingResult.reject("signupFailed", e.getMessage());
      
      // 아래는 일반 적인 개발에서 처리 방식
      // bindingResult.reject("signupFailed", "회원가입 중 오류가 발생했습니다. 다시 시도해주세요.");
      return "signup_form";
    }

    return "redirect:/";
  }

  @GetMapping("/login")
  public String login() {
    return "login_form";
  }

  @GetMapping("/getUserInfo")
  @ResponseBody
  public String getUserInfo(Principal principal) {
    String username = principal.getName();
    return "현재 로그인한 사용자 : " + username;
  }

  @GetMapping("/detailUserInfo")
  @ResponseBody
  public String getDetailUserInfo(Authentication authentication) {
    String username = authentication.getName();
    String roles = authentication.getAuthorities().toString();
    return "사용자 : %s, 권한 : %s".formatted(username, roles);
  }
}

package com.sbs.qnaService.boudedContext.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 컨트롤러로서의 역할을 부여
public class MainController {
  @GetMapping("/main") // "/main" 경로로 GET 요청을 처리
  @ResponseBody // 응답결과를 본문에 직접 작성
  public String showMain() {
    return "안녕하세요. 스프링부트에 오신 것을 환영합니다.";
  }

  // 루트 경로("/")로 접근 시 "/question/list"로 리다이렉트
  @GetMapping("/")
  public String root() {
    return "redirect:/question/list";
  }
}


package com.sbs.qnaService.boudedContext.question.controller;

import com.sbs.qnaService.boudedContext.answer.form.AnswerForm;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.form.QuestionForm;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import com.sbs.qnaService.boudedContext.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

  private final UserService userService;
  private final QuestionService questionService;

  @GetMapping("/list")
  public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
    Page<Question> paging = questionService.getList(page);
    model.addAttribute("paging", paging);

    return "question_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model,
                       @PathVariable("id") Integer id,
                       AnswerForm answerForm) {
    Question question = questionService.getQuestion(id);
    // Model 객체를 이용하여 뷰에 데이터 전달
    model.addAttribute("question", question);

    return "question_detail";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/create")
  public String questionCreate(QuestionForm questionForm) {
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create")
  public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
    // 에러를 가지고 있으면 true, 없으면 false
    if (bindingResult.hasErrors()) {
      // 에러가 있으면 question_form.html로 돌아간다.
      return "question_form";
    }

    // Principal : 현재 로그인한 사용자의 정보를 담고 있는 객체
    SiteUser siteUser = userService.getUser(principal.getName());
    questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
    // TODO 질문을 저장한다.
    return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
  }
}

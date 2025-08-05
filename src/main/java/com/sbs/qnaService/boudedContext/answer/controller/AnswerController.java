package com.sbs.qnaService.boudedContext.answer.controller;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import com.sbs.qnaService.boudedContext.answer.form.AnswerForm;
import com.sbs.qnaService.boudedContext.answer.service.AnswerService;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import com.sbs.qnaService.boudedContext.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {
  private final UserService userService;
  private final QuestionService questionService;
  private final AnswerService answerService;

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/create/{id}")
  public String createAnswer(Model model,
                             @PathVariable("id") Integer id,
                             @Valid AnswerForm answerForm,
                             BindingResult bindingResult, Principal principal) {
    // 관련 질문을 얻어온다.
    Question question = questionService.getQuestion(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("question", question);
      return "question_detail";
    }

    SiteUser siteUser = userService.getUser(principal.getName());
    // TODO: 답변을 저장한다.
    Answer answer = answerService.create(question, answerForm.getContent(), siteUser);

    return "redirect:/question/detail/%s".formatted(id); // GET 방식으로 질문 상세 페이지로 리다이렉트
  }
}

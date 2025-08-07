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
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

  private final UserService userService;
  private final QuestionService questionService;

  @GetMapping("/list")
  public String list(Model model,
                     @RequestParam(value = "page", defaultValue = "0") int page,
                     @RequestParam(value = "kw", defaultValue = "") String kw) {
    Page<Question> paging = questionService.getList(page, kw);
    model.addAttribute("paging", paging);
    model.addAttribute("kw", kw);

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

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/modify/{id}")
  public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
    Question question = questionService.getQuestion(id);

    if(!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    questionForm.setSubject(question.getSubject());
    questionForm.setContent(question.getContent());
    return "question_form";
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/modify/{id}")
  public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Integer id) {
    if (bindingResult.hasErrors()) {
      return "question_form";
    }

    Question question = questionService.getQuestion(id);
    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    }

    questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
    return String.format("redirect:/question/detail/%s", id);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/delete/{id}")
  public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
    Question question = questionService.getQuestion(id);

    if (!question.getAuthor().getUsername().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
    }

    questionService.delete(question);
    return "redirect:/";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/vote/{id}")
  public String questionVote(Principal principal, @PathVariable("id") Integer id) {
    Question question = questionService.getQuestion(id);
    SiteUser siteUser = userService.getUser(principal.getName());

    questionService.vote(question, siteUser);
    return String.format("redirect:/question/detail/%s", id);
  }
}

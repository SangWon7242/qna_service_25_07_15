package com.sbs.qnaService.boudedContext.answer.controller;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

  private final QuestionService questionService;

  @PostMapping("/create/{id}")
  public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam(value="content") String content) {
    // 관련 질문을 얻어온다.
    Question question = questionService.getQuestion(id);
    // TODO: 답변을 저장한다.

    //  return String.format("redirect:/question/detail/%s", id);
    return "redirect:/question/detail/%s".formatted(id);
  }
}

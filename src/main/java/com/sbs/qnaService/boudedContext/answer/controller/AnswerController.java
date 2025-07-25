package com.sbs.qnaService.boudedContext.answer.controller;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import com.sbs.qnaService.boudedContext.answer.form.AnswerForm;
import com.sbs.qnaService.boudedContext.answer.service.AnswerService;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

  private final QuestionService questionService;
  private final AnswerService answerService;

  @PostMapping("/create/{id}")
  public String createAnswer(Model model,
                             @PathVariable("id") Integer id,
                             @Valid AnswerForm answerForm,
                             BindingResult bindingResult) {
    // 관련 질문을 얻어온다.
    Question question = questionService.getQuestion(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("question", question);
      return "question_detail";
    }

    // TODO: 답변을 저장한다.
    Answer answer = answerService.create(question, answerForm.getContent());

    return "redirect:/question/detail/%s".formatted(id); // GET 방식으로 질문 상세 페이지로 리다이렉트
  }
}

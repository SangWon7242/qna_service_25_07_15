package com.sbs.qnaService.boudedContext.question.controller;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.form.QuestionForm;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

  private final QuestionService questionService;

  @GetMapping("/list")
  public String list(Model model) {
    // Model 객체를 이용하여 뷰에 데이터 전달
    List<Question> questionList = questionService.findAll();
    model.addAttribute("questionList", questionList);
    
    return "question_list";
  }

  @GetMapping("/detail/{id}")
  public String detail(Model model, @PathVariable("id") Integer id) {
    Question question = questionService.getQuestion(id);
    // Model 객체를 이용하여 뷰에 데이터 전달
    model.addAttribute("question", question);

    return "question_detail";
  }

  @GetMapping("/create")
  public String questionCreate(QuestionForm questionForm) {
    return "question_form";
  }

  @PostMapping("/create")
  public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
    // 에러를 가지고 있으면 true, 없으면 false
    if (bindingResult.hasErrors()) {
      // 에러가 있으면 question_form.html로 돌아간다.
      return "question_form";
    }

    questionService.create(questionForm.getSubject(), questionForm.getContent());
    // TODO 질문을 저장한다.
    return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
  }
}

package com.sbs.qnaService.boudedContext.question.controller;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {

  private final QuestionService questionService;

  @GetMapping("/question/list")
  public String list(Model model) {
    // Model 객체를 이용하여 뷰에 데이터 전달
    List<Question> questionList = questionService.findAll();
    model.addAttribute("questionList", questionList);
    
    return "question_list";
  }

  @GetMapping("/question/detail/{id}")
  public String detail(Model model, @PathVariable("id") Integer id) {
    Question question = questionService.getQuestion(id);
    // Model 객체를 이용하여 뷰에 데이터 전달
    model.addAttribute("question", question);

    return "question_detail";
  }
}

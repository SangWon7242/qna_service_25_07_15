package com.sbs.qnaService.boudedContext.question.controller;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class QuestionController {
  // 생성자 주입을 통해 QuestionRepository를 주입받음
  private final QuestionRepository questionRepository;

  @GetMapping("/question/list")
  public String list(Model model) {
    // Model 객체를 이용하여 뷰에 데이터 전달
    List<Question> questionList = questionRepository.findAll();
    model.addAttribute("questionList", questionList);
    
    return "question_list";
  }
}

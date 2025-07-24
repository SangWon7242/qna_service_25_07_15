package com.sbs.qnaService.boudedContext.answer.service;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import com.sbs.qnaService.boudedContext.answer.repository.AnswerRepository;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AnswerService {
  private final AnswerRepository answerRepository;

  public Answer create(Question question, String content) {
    Answer answer = new Answer();
    answer.setContent(content);
    answer.setCreateDate(LocalDateTime.now());
    answer.setQuestion(question);
    answerRepository.save(answer);

    return answer;
  }
}

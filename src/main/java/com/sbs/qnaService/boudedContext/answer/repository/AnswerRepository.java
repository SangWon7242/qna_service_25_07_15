package com.sbs.qnaService.boudedContext.answer.repository;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}

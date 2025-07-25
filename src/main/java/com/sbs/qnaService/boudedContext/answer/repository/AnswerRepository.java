package com.sbs.qnaService.boudedContext.answer.repository;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
  @Modifying
  @Transactional
  @Query(value = "ALTER TABLE answer AUTO_INCREMENT = 1;", nativeQuery = true)
  void clearAutoIncrement();
}

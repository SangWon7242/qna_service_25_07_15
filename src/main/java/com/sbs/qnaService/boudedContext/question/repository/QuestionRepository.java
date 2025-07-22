package com.sbs.qnaService.boudedContext.question.repository;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
  Question findBySubject(String subject);

  Question findBySubjectAndContent(String subject, String content);

  List<Question> findBySubjectLike(String keyword);

  @Modifying // INSERT, UPDATE, DELETE 쿼리를 실행할 때 사용(데이터 변경 작업시에만 사용)
  @Transactional
  // nativeQuery = true : MySQL 쿼리 사용이 가능
  @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1;", nativeQuery = true)
  void clearAutoIncrement();
}
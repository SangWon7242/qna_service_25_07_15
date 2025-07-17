package com.sbs.qnaService.boudedContext.question.repository;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
  @Modifying
  @Query(value = "SET FOREIGN_KEY_CHECKS = 0", nativeQuery = true)
  void foreignKeyDisabled();

  // nativeQuery : sql 문법을 그대로 사용
  @Modifying
  @Query(value = "TRUNCATE TABLE question", nativeQuery = true)
  void truncate();

  @Modifying
  @Query(value = "SET FOREIGN_KEY_CHECKS = 1", nativeQuery = true)
  void foreignKeyEnabled();

  Question findBySubject(String subject);

  Question findBySubjectAndContent(String subject, String content);

  List<Question> findBySubjectLike(String keyword);
}
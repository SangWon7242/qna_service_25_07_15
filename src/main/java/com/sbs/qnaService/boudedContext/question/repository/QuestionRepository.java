package com.sbs.qnaService.boudedContext.question.repository;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository : JPA를 사용하여 데이터베이스와 상호작용하는 리포지토리 인터페이스
// - 기본적인 CRUD(Create, Read, Update, Delete) 메서드를 제공
// Question : 엔티티 클래스
// Integer : 엔티티의 ID 타입
public interface QuestionRepository extends JpaRepository<Question, Integer> {
}

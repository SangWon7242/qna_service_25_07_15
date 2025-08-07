package com.sbs.qnaService.boudedContext.question.service;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import com.sbs.qnaService.exception.DataNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {
  private final QuestionRepository questionRepository;

  private Specification<Question> search(String kw) {
    return new Specification<>() {
      private static final long serialVersionUID = 1L;

      // toPredicate : 쿼리의 조건을 정의
      // Root<Question> q : Question 엔티티의 루트 객체로, 얘를 통해 엔티티 필드 접근 가능
      // CriteriaQuery<?> query : 기준이 되는 쿼리 객체로 쿼리의 전체 구조를 정의
      // CriteriaBuilder cb : 쿼리 조건을 생성
      @Override
      public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
        query.distinct(true);  // 중복을 제거
        // Question 엔티티의 author 필드와 조인
        Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
        // Question 엔티티의 answerList 필드와 조인(답변 목록)
        Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
        // Answer 엔티티의 author 필드와 조인
        Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
        return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
            cb.like(q.get("content"), "%" + kw + "%"),      // 내용
            cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
            cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
            cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
      }
    };
  }

  public List<Question> findAll() {
    return questionRepository.findAll();
  }

  public Question getQuestion(Integer id) {
    // SELECT * FROM question WHERE id = ?;
    Optional<Question> question = questionRepository.findById(id);
    if (question.isPresent()) {
      return question.get();
    } else {
      throw new DataNotFoundException("question not found");
    }
  }

  public Question create(String subject, String content, SiteUser author) {
    Question q = new Question();
    q.setSubject(subject);
    q.setContent(content);
    q.setAuthor(author);
    q.setCreateDate(LocalDateTime.now());
    return questionRepository.save(q);
  }
  
  // Page<Question> : 페이징 된 질문 데이터를 가져옴
  public Page<Question> getList(int page, String kw) {
    // Sort.Order : 정렬 기준을 설정
    List<Sort.Order> sorts = new ArrayList<>();
    // sorts.add(Sort.Order.desc("id")); // id 기준 내림차순 정렬
    sorts.add(Sort.Order.desc("createDate")); // 작성일 기준 내림차순 정렬

    // page : 요청 된 페이지 번호(0부터 시작)
    // 10 : 페이지당 데이터 개수
    // Sort.by(sorts) : 정렬 기준을 적용
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 페이지 번호와 페이지당 항목 수 설정

    // 검색어가 없는 경우 유효성 검사
    if (kw == null || kw.trim().isEmpty()) {
      return questionRepository.findAll(pageable);
    }

    Specification<Question> spec = search(kw); // Where조건을 적용

    return questionRepository.findAll(spec, pageable);
  }

  public void modify(Question question, String subject, String content) {
    question.setSubject(subject);
    question.setContent(content);
    question.setModifyDate(LocalDateTime.now());
    questionRepository.save(question);
  }

  public void delete(Question question) {
    questionRepository.delete(question);
  }

  public void vote(Question question, SiteUser siteUser) {
    question.getVoter().add(siteUser);
    questionRepository.save(question);
  }
}

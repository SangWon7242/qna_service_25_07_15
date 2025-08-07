package com.sbs.qnaService.boudedContext.question.service;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import com.sbs.qnaService.exception.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class QuestionService {
  private final QuestionRepository questionRepository;

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
  public Page<Question> getList(int page) {
    // Sort.Order : 정렬 기준을 설정
    List<Sort.Order> sorts = new ArrayList<>();
    // sorts.add(Sort.Order.desc("id")); // id 기준 내림차순 정렬
    sorts.add(Sort.Order.desc("createDate")); // 작성일 기준 내림차순 정렬

    // page : 요청 된 페이지 번호(0부터 시작)
    // 10 : 페이지당 데이터 개수
    // Sort.by(sorts) : 정렬 기준을 적용
    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); // 페이지 번호와 페이지당 항목 수 설정
    return questionRepository.findAll(pageable);
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
}

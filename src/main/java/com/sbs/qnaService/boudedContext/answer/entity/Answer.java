package com.sbs.qnaService.boudedContext.answer.entity;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(columnDefinition = "TEXT")
  private String content;

  private LocalDateTime createDate;

  @ManyToOne
  @ToString.Exclude // ToString에서 무한 루프를 방지하기 위해 제외
  private Question question;
}
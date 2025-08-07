package com.sbs.qnaService.boudedContext.answer.entity;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

  private LocalDateTime modifyDate;

  @ManyToOne
  private SiteUser author;

  @ManyToMany
  Set<SiteUser> voter = new HashSet<>();

  public void addVoter(SiteUser siteUser) {
    voter.add(siteUser);
  }

  @ManyToOne
  @ToString.Exclude // ToString에서 무한 루프를 방지하기 위해 제외
  private Question question;
}
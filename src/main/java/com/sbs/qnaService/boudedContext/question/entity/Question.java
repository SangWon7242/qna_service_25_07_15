package com.sbs.qnaService.boudedContext.question.entity;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity // 엔티티로서의 역할을 부여
// 아래 클래스와 1:1 매칭되는 테이블이 DB에 없다면, 자동으로 생성
public class Question {
  @Id // Primary Key로 지정
  @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment 설정
  private Integer id;

  @Column(length = 200) // Varchar(200)로 설정
  private String subject;

  @Column(columnDefinition = "TEXT") // TEXT 타입으로 설정
  private String content;

  private LocalDateTime createDate; // datetime : 생성일시를 저장하는 필드

  // @OneToMany : 자바세상에서의 편의를 위해서 필드 생성
  // 이 녀석은 실제로 DB에 테이블을 생성하지 않음
  // DB는 리스트나 배열을 지원하지 않기 때문에
  // 만들어도 되고 만들지 않아도 된다.
  // 만약에 만들면 해당 객체와 관련된 답변을 찾을 때 편하다.
  @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private List<Answer> answerList = new ArrayList<>();

  // 객체 내부의 상태를 캡슐화할 수 있다.
  // 외부에서 객체의 상태를 직접 변경하지 못하도록 하고,
  // 메서드를 통해서만 상태를 변경할 수 있도록 한다.
  public void addAnswer(Answer answer) {
    answer.setQuestion(this); // 양방향 연관관계 설정
    answerList.add(answer);
  }
}

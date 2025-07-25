package com.sbs.qnaService.boudedContext.question.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm {
  @NotEmpty(message="제목은 필수항목입니다.")
  @Size(max=200, message="제목은 200자 이내로 입력해주세요.")
  private String subject;

  @NotEmpty(message="내용은 필수항목입니다.")
  @Size(max=20000, message="내용은 20,000자 이내로 입력해주세요.")
  private String content;
}

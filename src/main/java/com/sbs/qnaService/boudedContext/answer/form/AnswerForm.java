package com.sbs.qnaService.boudedContext.answer.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
  @NotEmpty(message = "내용은 필수항목입니다.")
  @Size(max = 10000, message = "내용은 10,000자 이내로 입력해주세요.")
  private String content;
}

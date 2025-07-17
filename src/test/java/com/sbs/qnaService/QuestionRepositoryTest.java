package com.sbs.qnaService;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

@SpringBootTest
@ActiveProfiles("test")  // 테스트 프로파일을 사용하여 테스트 환경 설정
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	@DisplayName("질문 데이터 2개 저장")
	void t1() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);  // 두번째 질문 저장
	}

}

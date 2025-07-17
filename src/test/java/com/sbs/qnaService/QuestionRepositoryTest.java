package com.sbs.qnaService;

import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")  // 테스트 프로파일을 사용하여 테스트 환경 설정
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@BeforeEach // 각 테스트 메서드 실행 전에 호출되는 메서드
	@Transactional
	@Rollback(false) // 테스트 후 롤백하지 않도록 설정
	void beforeEach() {
		questionRepository.foreignKeyDisabled(); // 외래 키 제약 조건 비활성화
		questionRepository.truncate();
		makeTestData();	// 테스트 데이터 생성 메서드 호출
		questionRepository.foreignKeyEnabled(); // 외래 키 제약 조건 활성화
	}

	private void makeTestData() {
		IntStream.rangeClosed(1, 2).forEach(i -> {
			Question q = new Question();
			q.setSubject("테스트 질문 " + i);
			q.setContent("테스트 내용 " + i);
			q.setCreateDate(LocalDateTime.now());
			questionRepository.save(q);  // 각 질문을 저장
		});
	}

	@Test
	@DisplayName("질문 데이터 2개 저장")
	@Transactional
	@Rollback(false)
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

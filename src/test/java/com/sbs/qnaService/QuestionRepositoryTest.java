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
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")  // 테스트 프로파일을 사용하여 테스트 환경 설정
@Transactional
@Rollback(false)
class QuestionRepositoryTest {

	@Autowired
	private QuestionRepository questionRepository;

	@BeforeEach // 각 테스트 메서드 실행 전에 호출되는 메서드\
	void beforeEach() {
		// deleteAll() : DELETE FROM question;
		questionRepository.deleteAll();

		// AUTO_INCREMENT 초기화
		// 흔적삭제(다음번 INSERT 시 id가 1부터 시작하도록 초기화)
		questionRepository.clearAutoIncrement();

		makeTestData();	// 테스트 데이터 생성 메서드 호출
	}

	private void makeTestData() {
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

	@Test
	@DisplayName("질문 데이터 2개 저장")
	void t1() {
		Question q3 = new Question();
		q3.setSubject("스프링부트에 대해서 알고 싶습니다.");
		q3.setContent("스프링부트가 무엇인가요?");
		q3.setCreateDate(LocalDateTime.now());
		questionRepository.save(q3);

		Question q4 = new Question();
		q4.setSubject("자바는 어떻게 시작하나요?");
		q4.setContent("자바를 처음 배우고 싶습니다.");
		q4.setCreateDate(LocalDateTime.now());
		questionRepository.save(q4);
	}

	/*
	SELECT *
	FROM question
	*/
	@Test
	@DisplayName("findAll 테스트")
	void t2() {
		List<Question> all = questionRepository.findAll();

		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/*
	SELECT *
	FROM question
	WHERE id = 1;
	*/
	@Test
	@DisplayName("findById 테스트")
	void t3() {
		Optional<Question> oq = questionRepository.findById(1);

		// isPresent : Optional 객체가 값을 가지고 있는지 확인
		if(oq.isPresent()) {
			Question q = oq.get();
			assertEquals("sbb가 무엇인가요?", q.getSubject());
		}
	}

	/*
	SELECT *
	FROM question
	WHERE subject = "sbb가 무엇인가요?";
	*/
	@Test
	@DisplayName("findBySubject 테스트")
	void t4() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");

		assertEquals(1, q.getId());
	}

	/*
	SELECT *
	FROM question
	WHERE subject = "sbb가 무엇인가요?"
	AND content = "sbb에 대해서 알고 싶습니다.";
	*/
	@Test
	@DisplayName("findBySubjectAndContent 테스트")
	void t5() {
		Question q = questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertEquals(1, q.getId());
	}

	/*
	SELECT *
	FROM question
	WHERE subject LIKE = 'sbb%';
	*/
	@Test
	@DisplayName("findBySubjectLike 테스트")
	void t6() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");

		Question q = qList.get(0);
		assertEquals("sbb가 무엇인가요?", q.getSubject());
	}

	/*
	UPDATE question
	SET create_date = NOW(),
	subject = ?,
	content = ?,
	WHERE id = ?;
	*/
	@Test
	@DisplayName("update 테스트")
	void t7() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());

		Question q = oq.get();
		q.setSubject("수정된 제목");
		q.setContent("수정된 내용");
		questionRepository.save(q); // save 메서드는 데이터가 존재하는 경우 업데이트를 수행한다.
	}

	/*
	DELETE
	FROM question
	WHERE id = ?;
	*/
	@Test
	@DisplayName("delete 테스트")
	void t8() {
		// SQL : SELECT COUNT(*) FROM question;
		assertEquals(2, questionRepository.count());

		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());

		Question q = oq.get();
		questionRepository.delete(q);
		assertEquals(1, questionRepository.count());
	}
}

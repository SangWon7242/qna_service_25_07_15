package com.sbs.qnaService;

import com.sbs.qnaService.boudedContext.answer.entity.Answer;
import com.sbs.qnaService.boudedContext.answer.repository.AnswerRepository;
import com.sbs.qnaService.boudedContext.answer.service.AnswerService;
import com.sbs.qnaService.boudedContext.question.entity.Question;
import com.sbs.qnaService.boudedContext.question.repository.QuestionRepository;
import com.sbs.qnaService.boudedContext.question.service.QuestionService;
import com.sbs.qnaService.boudedContext.user.entity.SiteUser;
import com.sbs.qnaService.boudedContext.user.repository.UserRepository;
import com.sbs.qnaService.boudedContext.user.service.UserService;
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
@Rollback(false)
class QnaServiceApplicationTest {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerRepository answerRepository;

  @Autowired
  private AnswerService answerService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach // 각 테스트 메서드 실행 전에 호출되는 메서드\
	void beforeEach() {
		// 답변데이터 초기화
		answerRepository.deleteAll();
		answerRepository.clearAutoIncrement();

		// deleteAll() : DELETE FROM question;
		questionRepository.deleteAll();

		// AUTO_INCREMENT 초기화
		// 흔적삭제(다음번 INSERT 시 id가 1부터 시작하도록 초기화)
		questionRepository.clearAutoIncrement();

		userRepository.deleteAll();
		userRepository.clearAutoIncrement();

		makeTestData();	// 테스트 데이터 생성 메서드 호출
	}

	private void makeTestData() {
    // 회원 2명 생성
    userService.create("user1", "user1@test.com", "1234");
    userService.create("user2", "user2@test.com", "1234");

		SiteUser user1 = userService.getUser("user1");
    SiteUser user2 = userService.getUser("user2");
    
    Question q1 = questionService.create("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.", user1);
		Question q2 = questionService.create("스프링부트 모델 질문입니다.", "id는 자동으로 생성되나요?", user2);
    
    // 2번 질문에 대한 첫 번째 답변 생성
		Answer a1 = answerService.create(q2, "네 자동으로 생성됩니다.", user1);

    q1.addVoter(user1);
    q1.addVoter(user2);
    questionRepository.save(q1);

    q2.addVoter(user1);
    q2.addVoter(user2);
    questionRepository.save(q2);

    a1.addVoter(user1);
    a1.addVoter(user2);
    answerRepository.save(a1);
	}

	@Test
	@DisplayName("질문 데이터 2개 저장")
	void t1() {
		SiteUser user1 = userService.getUser("user1");
		Question q3 = new Question();
		q3.setSubject("스프링부트에 대해서 알고 싶습니다.");
		q3.setContent("스프링부트가 무엇인가요?");
		q3.setAuthor(user1);
		q3.setCreateDate(LocalDateTime.now());
		questionRepository.save(q3);

		SiteUser user2 = userService.getUser("user2");
		Question q4 = new Question();
		q4.setSubject("자바는 어떻게 시작하나요?");
		q4.setContent("자바를 처음 배우고 싶습니다.");
		q4.setAuthor(user2);
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

	/*
	// 질문 조회
	SELECT *
	FROM question
	WHERE id = ?;
	
	// 조회한 질문에 대한 답변 데이터 생성
	INSERT INTO answer
	SET create_date = NOW,
	content = ?,
	question_id = ?;
	*/
	@Test
	@DisplayName("답변 데이터 생성 후 저장")
	void t9() {
		// v1
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		// v2
		// Question q = questionRepository.findById(2).get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
	}

	@Test
	@DisplayName("답변 데이터 조회")
	void t10() {
		// SELECT * FROM answer WHERE id = 1;
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Test
	@DisplayName("질문을 통해 답변 조회")
	@Transactional
	void t11() {
		System.out.println("t11() 실행");
		// SELECT * FROM question WHERE id = 2;
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		// 테스트 환경에서는 get해서 가져온 뒤 DB 연결을 끊음
		
		// 질문에 있는 답변 목록을 가져옴
		// SELECT * FROM answer WHERE question_id = 2;
		List<Answer> answerList = q.getAnswerList(); // DB 통신이 끊긴 다음에 가져오기 때문에 실행이 불가능

		// 질문에 대한 답변이 1개가 저장되어 있으므로, answerList의 크기는 1이어야 한다.
		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	@DisplayName("대량의 테스트 데이터 생성")
	void t12() {
		SiteUser user2 = userService.getUser("user2");
		IntStream.rangeClosed(3, 300)
				.forEach(no ->
						questionService.create("테스트 제목입니다. %d".formatted(no),
								"테스트 내용입니다. %d".formatted(no), user2));
	}
}

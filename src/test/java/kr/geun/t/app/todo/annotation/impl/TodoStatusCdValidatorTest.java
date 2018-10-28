package kr.geun.t.app.todo.annotation.impl;

import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * 할일 상태값에 대한 Valid Testcase
 *
 * @akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoStatusCdValidatorTest {

    private Validator validator;

    /**
     * Test 실행 전 동작
     */
    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * 잘못된 String 을 입력하고 잘 실패하는지 확인
     */
    @Test
    public void testFailTodoStatusInvalidChk() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.ModifyStatus invalidStringParam = TodoDTO.ModifyStatus.builder()
			.content("테스트")
			.statusCd("PROGRESSING")
			.build();
		//@formatter:on

        //WHEN(Execution)
        Set<ConstraintViolation<TodoDTO.ModifyStatus>> violations = validator.validate(invalidStringParam);

        //THEN(Verification)
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    /**
     * 공백, null, white Space 에 해당하는 값을 넣었을 때 실패하는지 확인
     */
    @Test
    public void testFailTodoStatusBlank() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.ModifyStatus nullParam = TodoDTO.ModifyStatus.builder()
			.content("테스트")
			.statusCd(null) //NULL
			.build();

		TodoDTO.ModifyStatus blankParam = TodoDTO.ModifyStatus.builder()
			.content("테스트")
			.statusCd("") //BLANK
			.build();

		TodoDTO.ModifyStatus whiteSpaceParam = TodoDTO.ModifyStatus.builder()
			.content("테스트")
			.statusCd(" ") //WHITE SPACE
			.build();
		//@formatter:on

        //WHEN(Execution)
        Set<ConstraintViolation<TodoDTO.ModifyStatus>> nullValid = validator.validate(nullParam);
        Set<ConstraintViolation<TodoDTO.ModifyStatus>> blankValid = validator.validate(blankParam);
        Set<ConstraintViolation<TodoDTO.ModifyStatus>> whiteSpaceValid = validator.validate(whiteSpaceParam);

        //THEN(Verification)
        assertFalse(nullValid.isEmpty());
        assertEquals(1, nullValid.size());

        assertFalse(blankValid.isEmpty());
        assertEquals(1, blankValid.size());

        assertFalse(whiteSpaceValid.isEmpty());
        assertEquals(1, whiteSpaceValid.size());
    }

    /**
     * 정상 동작하는지 확인
     */
    @Test
    public void testSuccessValid() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.ModifyStatus successParam = TodoDTO.ModifyStatus.builder()
			.content("테스트")
			.statusCd(TodoStatusCd.NOT_YET.name())
			.build();
		//@formatter:on

        //WHEN(Execution)
        Set<ConstraintViolation<TodoDTO.ModifyStatus>> successValid = validator.validate(successParam);

        //THEN(Verification)
        assertTrue(successValid.isEmpty());
        assertEquals(0, successValid.size());
    }
}

package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.config.EhCacheConfig;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * 할일 수정에 대한 Test Case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@Import(EhCacheConfig.class)
@SpringBootTest
public class TodoApiServiceModifyTest {

	@Autowired
	private TodoApiService todoApiService;

	@MockBean
	private TodoRepository mockTodoRepository;

	/**
	 * 자기 자신을 참조 할 수 없는 기능 테스트
	 */
	@Test
	public void testFailModifySelfRef() {
		//GIVEN(Preparation)

		//@formatter:off
        final Long mockTodoId = 1L;

        TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(mockTodoId)
            .content("빨래")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .refTodos(new Long[]{mockTodoId})
			.build();

        TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(mockTodoId)
            .content(mockParam.getContent())
            .statusCd(mockParam.getStatusCd())
                .build();

		//@formatter:on

		given(todoApiService.get(mockTodoId)).willReturn(mockTodoEntity);

		ResData<TodoEntity> result = todoApiService.isChkSelfRef(mockParam.getTodoId(), mockParam.getRefTodos());

		//THEN(Verification)
		assertFalse(result.getResult());
		assertNotNull(result);

	}

	/**
	 * 수정 시 사용할 전처리 로직 성공 테스트
	 */
	@Test
	public void testSuccessPreModify() {
		//GIVEN(Preparation)
		final Long mockTodoId = 1L;

		//@formatter:off
		TodoEntity mockTodoEntity = TodoEntity.builder()
			.todoId(mockTodoId)
			.content("집안일")
			.statusCd(TodoStatusCd.NOT_YET.name())
			.build();

		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(mockTodoEntity.getTodoId())
			.content(mockTodoEntity.getContent())
			.statusCd(mockTodoEntity.getStatusCd())
			.build();
		//@formatter:on

		given(todoApiService.get(mockTodoId)).willReturn(mockTodoEntity);

		ResData<TodoEntity> result = todoApiService.isChkSelfRef(mockParam.getTodoId(), mockParam.getRefTodos());

		//THEN(Verification)
		assertTrue(result.getResult());
		assertNotNull(result);
	}
}

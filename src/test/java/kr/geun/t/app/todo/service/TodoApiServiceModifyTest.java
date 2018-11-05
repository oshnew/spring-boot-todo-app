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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	 * 수정 시 저장되어 있지 않은 할일 아이디를 보낼 경우 실패 테스트
	 */
	@Test
	public void testFailPreModify() {
		//GIVEN(Preparation)

		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(1L)
			.content("테스트")
			.statusCd(TodoStatusCd.NOT_YET.name())
			.build();
		//@formatter:on
		given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(null);

		//WHEN(Execution)
		ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModify(mockParam);
		ResData<TodoEntity> resultBody = result.getBody();

		//THEN(Verification)
		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
		assertNull(resultBody.getData());
	}

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
            .todoId(1L)
            .content(mockParam.getContent())
            .statusCd(mockParam.getStatusCd())
                .build();



		//@formatter:on

		given(mockTodoRepository.findOne(mockTodoId)).willReturn(mockTodoEntity);

		//WHEN(Execution)
		ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModify(mockParam);
		ResData<TodoEntity> resultBody = result.getBody();
		TodoEntity todoEntity = resultBody.getData();

		//THEN(Verification)
		assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
		assertNull(todoEntity);

	}

	/**
	 * 수정 시 사용할 전처리 로직 성공 테스트
	 */
	@Test
	public void testSuccessPreModify() {
		//GIVEN(Preparation)

		TodoEntity mockTodoEntity = TodoEntity.builder().todoId(1L).content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build();
		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(mockTodoEntity.getTodoId())
			.content(mockTodoEntity.getContent())
			.statusCd(mockTodoEntity.getStatusCd())
			.build();
		//@formatter:on

		given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(mockTodoEntity);

		//WHEN(Execution)
		ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModify(mockParam);
		ResData<TodoEntity> resultBody = result.getBody();
		TodoEntity todoEntity = resultBody.getData();

		//THEN(Verification)
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNull(todoEntity);
	}

	/**
	 * 수정 성공 테스트
	 */
	@Test
	public void testSuccessModify() {
		//GIVEN(Preparation)
		TodoEntity mockTodoEntity = TodoEntity.builder().todoId(1L).content("변경 테스트").statusCd(TodoStatusCd.NOT_YET.name()).build();
		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(mockTodoEntity.getTodoId())
			.content(mockTodoEntity.getContent())
			.statusCd(mockTodoEntity.getStatusCd())
			.build();
		//@formatter:on

		given(mockTodoRepository.save(mockTodoEntity)).willReturn(mockTodoEntity);

		//WHEN(Execution)
		ResponseEntity<ResData<TodoEntity>> result = todoApiService.modify(mockParam);
		ResData<TodoEntity> resultBody = result.getBody();
		TodoEntity todoEntity = resultBody.getData();

		//THEN(Verification)
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertNotNull(todoEntity);
		assertEquals(mockParam.getTodoId(), todoEntity.getTodoId());
		assertEquals(mockParam.getContent(), todoEntity.getContent());

	}
}

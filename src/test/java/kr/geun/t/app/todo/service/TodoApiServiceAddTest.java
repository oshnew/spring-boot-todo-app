package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.config.EhCacheConfig;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRefRepository;
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

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * 할일 쓰기에 대한 Test Case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@Import(EhCacheConfig.class)
@SpringBootTest
public class TodoApiServiceAddTest {

	@MockBean
	private TodoRepository todoRepository;

	@MockBean
	private TodoRefRepository todoRefRepository;

	@Autowired
	private TodoApiService todoApiService;

	/**
	 * 할일만 입력할 경우에 대한 성공 테스트
	 */
	@Test
	public void testSuccessAddOnlyTodo() {
		//GIVEN(Preparation)
		LocalDateTime ldt = LocalDateTime.now();

		//@formatter:off
        TodoEntity mockSaveParam = TodoEntity.builder()
            .content("집안일")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .build();

        TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(1L)
            .content(mockSaveParam.getContent())
            .statusCd(mockSaveParam.getStatusCd())
            .createdAt(ldt)
            .updatedAt(ldt)
                .build();

		TodoDTO.Add mockParam = TodoDTO.Add.builder()
			.content(mockSaveParam.getContent())
			.statusCd(mockSaveParam.getStatusCd())
			.build();
		//@formatter:on

		given(todoRepository.save(mockSaveParam)).willReturn(mockTodoEntity);

		//WHEN(Execution)
		TodoEntity result = todoApiService.add(mockParam.getContent(), new Long[] {});

		//THEN(Verification)
		//assertEquals("성공했습니다.", resultBody.getMsg());
		assertNotNull(result);
	}

	/**
	 * 할일과 참조가 함께 추가되는 경우의 성공 테스트
	 */
	@Test
	public void testSuccessAddWithTodoRef() {
		//GIVEN(Preparation)
		LocalDateTime ldt = LocalDateTime.now();

		//@formatter:off
        TodoEntity mockSaveParam = TodoEntity.builder()
            .content("빨래")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .build();

        TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(1L)
            .content(mockSaveParam.getContent())
            .statusCd(mockSaveParam.getStatusCd())
            .createdAt(ldt)
            .updatedAt(ldt)
                .build();

		TodoDTO.Add mockParam = TodoDTO.Add.builder()
			.content(mockSaveParam.getContent())
			.statusCd(mockSaveParam.getStatusCd())
            .refTodos(new Long[]{2L})
			.build();

		TodoRefEntity mockTodoRefParam = TodoRefEntity.builder().parentTodoId(mockTodoEntity.getTodoId()).refTodoId(2L).build();
		TodoRefEntity mockTodoRefEntity = TodoRefEntity.builder().parentTodoId(mockTodoEntity.getTodoId()).refTodoId(2L).build();
		//@formatter:on

		given(todoRepository.save(mockSaveParam)).willReturn(mockTodoEntity);
		given(todoRefRepository.save(mockTodoRefParam)).willReturn(mockTodoRefEntity);

		TodoEntity result = todoApiService.add(mockParam.getContent(), mockParam.getRefTodos());

		assertNotNull(result);
	}
}

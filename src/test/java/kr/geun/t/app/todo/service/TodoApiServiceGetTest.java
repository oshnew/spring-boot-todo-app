package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * 단건조회에 대한 Test case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceGetTest {

    @MockBean
    private TodoRepository mockTodoRepository;

    @Autowired
    private TodoApiService todoApiService;

    /**
     * 없는 할일을 가져왔을 때 실패 테스트
     */
    @Test
    public void testFailGetTodo() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get mockParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();
		//@formatter:on
        given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(null);

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(mockParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(resultBody.getData());
    }

    /**
     * 할일만 입력된 할일 가져오기 성공
     */
    @Test
    public void testSuccessGetOnlyTodo() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get mockParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();
		//@formatter:on
        TodoEntity mockTodoEntity = TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build();

        given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(mockTodoEntity);

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(mockParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(mockTodoEntity.getContent(), todoEntity.getContent());
    }

    /**
     * 할일과 참조가 걸린 할일까지 함께해서 가져오기 성공
     */
    @Test
    public void testSuccessGetWithTodoRef() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get mockParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();

		TodoEntity mockTodoEntity = TodoEntity.builder()
            .content("집안일")
            .statusCd(TodoStatusCd.NOT_YET.name())
                .todoRefs(Arrays.asList(TodoRefEntity.builder().parentTodoId(mockParam.getTodoId()).refTodoId(2L).build()))
            .build();

		//@formatter:on

        given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(mockTodoEntity);

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(mockParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(mockTodoEntity.getContent(), todoEntity.getContent());
        assertEquals(1, todoEntity.getTodoRefs().size());

        assertEquals(mockTodoEntity.getTodoRefs().get(0).getRefTodoId(), todoEntity.getTodoRefs().get(0).getRefTodoId());
    }
}

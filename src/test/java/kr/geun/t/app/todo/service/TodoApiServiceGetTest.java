package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * 단건조회에 대한 Test case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceGetTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoRefRepository todoRefRepository;

    @Autowired
    private TodoApiService todoApiService;

    /**
     * 없는 할일을 가져왔을 때 실패 테스트
     */
    @Test
    public void testFailGetTodo() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get dbParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(dbParam);
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
        TodoEntity preGetInfo1 = todoRepository.save(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build());
        //@formatter:off
		TodoDTO.Get dbParam = TodoDTO.Get.builder()
            .todoId(preGetInfo1.getTodoId())
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(preGetInfo1.getContent(), todoEntity.getContent());
    }

    /**
     * 할일과 참조가 걸린 할일까지 함께해서 가져오기 성공
     */
    @Test
    public void testSuccessGetWithTodoRef() {
        //GIVEN(Preparation)
        TodoEntity preGetInfo1 = todoRepository.save(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build());
        TodoEntity preGetInfo2 = todoRepository.save(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).build());

        TodoRefEntity preRefInfo1 = todoRefRepository.save(
            TodoRefEntity.builder().parentTodoId(preGetInfo2.getTodoId()).refTodoId(preGetInfo1.getTodoId()).build());

        //@formatter:off
		TodoDTO.Get dbParam = TodoDTO.Get.builder()
            .todoId(preGetInfo2.getTodoId())
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.get(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(preGetInfo2.getContent(), todoEntity.getContent());
        assertEquals(1, todoEntity.getTodoRefs().size());

        assertEquals(preRefInfo1.getRefId(), todoEntity.getTodoRefs().get(0).getRefId());
        assertEquals(preGetInfo1.getTodoId(), todoEntity.getTodoRefs().get(0).getRefTodoId());
    }
}

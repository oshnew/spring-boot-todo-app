package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
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
 * 할일 수정에 대한 Test Case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceModifyTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoApiService todoApiService;

    /**
     * 수정 시 저장되어 있지 않은 할일 아이디를 보낼 경우 실패 테스트
     */
    @Test
    public void testFailPreModify() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(1L)
			.content("테스트")
			.statusCd(TodoStatusCd.NOT_YET.name())
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModify(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(resultBody.getData());
    }

    /**
     * 수정 시 사용할 전처리 로직 성공 테스트
     */
    @Test
    public void testSuccessPreModify() {
        //GIVEN(Preparation)
        TodoEntity preInfo = todoRepository.save(TodoEntity.builder().content("테스트").statusCd(TodoStatusCd.NOT_YET.name()).build());

        //@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(preInfo.getTodoId())
			.content(preInfo.getContent())
			.statusCd(preInfo.getStatusCd())
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModify(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(dbParam.getTodoId(), todoEntity.getTodoId());
    }

    /**
     * 수정 성공 테스트
     */
    @Test
    public void testSuccessModify() {
        //GIVEN(Preparation)
        TodoEntity preInfo = todoRepository.save(TodoEntity.builder().content("테스트").statusCd(TodoStatusCd.NOT_YET.name()).build());

        //@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(preInfo.getTodoId())
			.content("변경 테스트")
			.statusCd(preInfo.getStatusCd())
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.modify(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoEntity);
        assertEquals(dbParam.getTodoId(), todoEntity.getTodoId());
        assertEquals(dbParam.getContent(), todoEntity.getContent());
        assertNotEquals(preInfo.getContent(), todoEntity.getContent());

    }
}

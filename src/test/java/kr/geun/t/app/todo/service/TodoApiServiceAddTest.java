package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRefRepository;
import kr.geun.t.app.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 할일 쓰기에 대한 Test Case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceAddTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoRefRepository todoRefRepository;

    @Autowired
    private TodoApiService todoApiService;

    private TodoEntity preAddInfo;

    /**
     * Test 실행 전 동작
     */
    @Before
    public void setUp() throws Exception {
        preAddInfo = todoRepository.save(TodoEntity.builder().content("집안일").statusCd("NOT_YET").build());

    }

    /**
     * 할일만 입력할 경우에 대한 성공 테스트
     */
    @Test
    public void testSuccessOnlyTodo() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Add dbParam = TodoDTO.Add.builder()
			.content("테스트")
			.statusCd("NOT_YET")
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.add(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        TodoEntity dbInfo = todoRepository.findOne(todoEntity.getTodoId());

        //THEN(Verification)
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        //assertEquals("성공했습니다.", resultBody.getMsg());
        assertNotNull(dbInfo);
        assertEquals(dbParam.getContent(), dbInfo.getContent());

    }

    /**
     * 할일과 참조가 함께 추가되는 경우의 성공 테스트
     */
    @Test
    public void testSuccessWithTodoRef() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Add dbParam = TodoDTO.Add.builder()
			.content("빨래")
			.statusCd("NOT_YET")
            .refTodos(new Long[]{preAddInfo.getTodoId()})
			.build();
		//@formatter:on

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.add(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();
        TodoEntity todoEntity = resultBody.getData();

        TodoEntity dbInfo = todoRepository.findOne(todoEntity.getTodoId());
        List<TodoRefEntity> refEntityList = todoRefRepository.findByParentTodoId(todoEntity.getTodoId());

        //THEN(Verification)
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        //assertEquals("성공했습니다.", resultBody.getMsg());
        assertNotNull(dbInfo);
        assertEquals(dbParam.getContent(), dbInfo.getContent());
        assertEquals(1, refEntityList.size());
        assertEquals(preAddInfo.getTodoId(), refEntityList.get(0).getRefTodoId());
    }
}

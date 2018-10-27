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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

/**
 * 상태값 변경에 대한 Test case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceModifyStatusTest {

    @Autowired
    private TodoApiService todoApiService;

    @MockBean
    private TodoRepository mockTodoRepository;

    /**
     * 데이터를 찾을 수 없는 경우 실패하는지 테스트
     */
    @Test
    public void testFailNotFoundData() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.ModifyStatus dbParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();
		//@formatter:on
        given(mockTodoRepository.findOne(dbParam.getTodoId())).willReturn(null);

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModifyStatus(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNull(resultBody.getData());
    }

    /**
     * 아직 미처리한 참조가 있을 경우 실패하는지 테스트
     */
    @Test
    public void testFailNotYetTodoExist() {

        //@formatter:off
		TodoDTO.ModifyStatus dbParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();

		TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(dbParam.getTodoId())
            .statusCd(TodoStatusCd.NOT_YET.name())
                .todoRefs(Arrays.asList(
                    TodoRefEntity.builder().parentTodoId(dbParam.getTodoId()).refTodoId(2L)
                        .todoRefsInfo(TodoEntity.builder().statusCd(TodoStatusCd.NOT_YET.name()).build())
                        .build()
                ))
            .build();
		//@formatter:on

        given(mockTodoRepository.findOne(dbParam.getTodoId())).willReturn(mockTodoEntity);

        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModifyStatus(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNull(resultBody.getData());

    }

    /**
     * 성공 테스트
     */
    @Test
    public void testSuccessCompleteUpdate() {
        //@formatter:off
		TodoDTO.ModifyStatus dbParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
            .content("집안일")
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();

		TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(dbParam.getTodoId())
            .content(dbParam.getContent())
            .statusCd(dbParam.getStatusCd())
            .build();
		//@formatter:on

        given(mockTodoRepository.save(mockTodoEntity)).willReturn(mockTodoEntity);

        ResponseEntity<ResData<TodoEntity>> result = todoApiService.modifyStatus(dbParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(resultBody.getData());

    }

}

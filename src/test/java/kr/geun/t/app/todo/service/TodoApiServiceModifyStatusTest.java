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
@Import(EhCacheConfig.class)
@SpringBootTest
public class TodoApiServiceModifyStatusTest {

    @Autowired
    private TodoApiService todoApiService;

    @MockBean
    private TodoRepository mockTodoRepository;

    @MockBean
    private TodoRefRepository todoRefRepository;

    /**
     * 데이터를 찾을 수 없는 경우 실패하는지 테스트
     */
    @Test
    public void testFailNotFoundData() {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.ModifyStatus mockParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();
		//@formatter:on
        given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(null);

        //WHEN(Execution)
        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModifyStatus(mockParam);
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
		TodoDTO.ModifyStatus mockParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();

		TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(mockParam.getTodoId())
            .statusCd(TodoStatusCd.NOT_YET.name())
            .build();

		given(todoRefRepository.findByRefTodoId(mockParam.getTodoId())).willReturn(
            Arrays.asList(
                TodoRefEntity.builder().parentTodoId(mockParam.getTodoId()).refTodoId(2L)
                    .todoJoinInfo(TodoEntity.builder().statusCd(TodoStatusCd.NOT_YET.name()).build())
                    .build())
        );
		//@formatter:on
        given(mockTodoRepository.findOne(mockParam.getTodoId())).willReturn(mockTodoEntity);


        ResponseEntity<ResData<TodoEntity>> result = todoApiService.preModifyStatus(mockParam);
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
		TodoDTO.ModifyStatus mockParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
            .content("집안일")
			.statusCd(TodoStatusCd.COMPLETE.name())
			.build();

		TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(mockParam.getTodoId())
            .content(mockParam.getContent())
            .statusCd(mockParam.getStatusCd())
            .build();
		//@formatter:on

        given(mockTodoRepository.save(mockTodoEntity)).willReturn(mockTodoEntity);

        ResponseEntity<ResData<TodoEntity>> result = todoApiService.modifyStatus(mockParam);
        ResData<TodoEntity> resultBody = result.getBody();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNull(resultBody.getData());

    }

}

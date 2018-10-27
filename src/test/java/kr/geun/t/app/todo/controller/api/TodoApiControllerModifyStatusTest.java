package kr.geun.t.app.todo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRefRepository;
import kr.geun.t.app.todo.repository.TodoRepository;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 수정관련 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class, TodoApiService.class})
public class TodoApiControllerModifyStatusTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TodoRefRepository todoRefRepository;

	@MockBean
	private TodoRepository todoRepository;

	private static final ObjectMapper OM = new ObjectMapper();

	/**
	 * 파라미터 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailParameterErr() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
        TodoDTO.ModifyStatus dbParam = TodoDTO.ModifyStatus.builder()
            .todoId(1L)
            .build();

        mvc.perform(
            //WHEN(Execution)
            patch("/api/v1/todo/{id}", dbParam.getTodoId())
                .param("statusCd", "TEST")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}

	/**
	 * 수정 성공 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testSuccessModifyStatus() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .statusCd(TodoStatusCd.COMPLETE.name())
            .build();

		//@formatter:on

		TodoEntity preDataEntity = TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build();
		TodoEntity inputDataEntity = TodoEntity.builder().todoId(dbParam.getTodoId()).content(preDataEntity.getContent()).statusCd(
			dbParam.getStatusCd()).build();

		given(todoRepository.findOne(dbParam.getTodoId())).willReturn(preDataEntity);
		given(todoRepository.save(inputDataEntity)).willReturn(inputDataEntity);

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            patch("/api/v1/todo/{id}", dbParam.getTodoId())
                .param("statusCd", dbParam.getStatusCd())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(OM.writeValueAsString(dbParam)))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}

	/**
	 * 상태값 변경 중 참조걸린 할일 중 미처리인 할일이 있을경우 실패 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailModifyWithRef() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .statusCd(TodoStatusCd.COMPLETE.name())
            .refTodos(new Long[]{2L, 3L})
            .build();

		//@formatter:on

		List<TodoRefEntity> refEntities = new ArrayList<>();
		for (Long refTodo : dbParam.getRefTodos()) {
			TodoStatusCd tmpStatus = TodoStatusCd.NOT_YET;
			if (refTodo == 2L) {
				tmpStatus = TodoStatusCd.COMPLETE;
			}

			refEntities.add(TodoRefEntity.builder().parentTodoId(dbParam.getTodoId()).refTodoId(refTodo).todoRefsInfo(
				TodoEntity.builder().statusCd(tmpStatus.name()).build()).build());
		}

		TodoEntity preDataEntity = TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).todoRefs(refEntities).build();
		TodoEntity inputDataEntity = TodoEntity.builder().todoId(dbParam.getTodoId()).content(preDataEntity.getContent()).statusCd(
			dbParam.getStatusCd()).build();

		given(todoRepository.findOne(dbParam.getTodoId())).willReturn(preDataEntity);
		given(todoRepository.save(inputDataEntity)).willReturn(inputDataEntity);

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            patch("/api/v1/todo/{id}", dbParam.getTodoId())
                .param("statusCd", dbParam.getStatusCd())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}

	/**
	 * 상태값 변경 성공
	 *
	 * @throws Exception
	 */
	@Test
	public void testSuccessModifyWithRef() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify dbParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .statusCd(TodoStatusCd.COMPLETE.name())
            .refTodos(new Long[]{2L, 3L})
            .build();

		//@formatter:on

		List<TodoRefEntity> refEntities = new ArrayList<>();
		for (Long refTodo : dbParam.getRefTodos()) {
			refEntities.add(TodoRefEntity.builder().parentTodoId(dbParam.getTodoId()).refTodoId(refTodo).todoRefsInfo(
				TodoEntity.builder().statusCd(TodoStatusCd.COMPLETE.name()).build()).build());
		}

		TodoEntity preDataEntity = TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).todoRefs(refEntities).build();
		TodoEntity inputDataEntity = TodoEntity.builder().todoId(dbParam.getTodoId()).content(preDataEntity.getContent()).statusCd(
			dbParam.getStatusCd()).build();

		given(todoRepository.findOne(dbParam.getTodoId())).willReturn(preDataEntity);
		given(todoRepository.save(inputDataEntity)).willReturn(inputDataEntity);

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            patch("/api/v1/todo/{id}", dbParam.getTodoId())
                .param("statusCd", dbParam.getStatusCd())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}
}

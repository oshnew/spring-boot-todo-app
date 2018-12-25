package kr.geun.t.app.todo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 수정관련 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class})
public class TodoApiControllerModifyTest {

	@Autowired
	private MockMvc mvc;

	//	@MockBean
	//	private TodoRefRepository todoRefRepository;
	//
	//	@MockBean
	//	private TodoRepository todoRepository;

	@MockBean
	private TodoApiService todoApiService;

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
        TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(1L)
//            .content("집안일")
//            .statusCd(TodoStatusCd.NOT_YET.name())
            .refTodos(new Long[]{1L, 2L})
            .build();

        mvc.perform(
            //WHEN(Execution)
            put("/api/v1/todo/{id}", mockParam.getTodoId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(OM.writeValueAsString(mockParam)))
            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}


	/**
	 * 파라미터 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailParameterErr_withNotFound() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		final Long mockTodoId = 1L;

		given(todoApiService.get(mockTodoId)).willReturn(null);

        mvc.perform(
            //WHEN(Execution)
            put("/api/v1/todo/{id}", mockTodoId)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                )
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
	public void testSuccessModify() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .content("집안일2")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .build();

		//@formatter:on

		TodoEntity preDataEntity = TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build();
		TodoEntity inputDataEntity = TodoEntity.builder().todoId(mockParam.getTodoId()).content(mockParam.getContent()).statusCd(
			mockParam.getStatusCd()).build();

		given(todoApiService.get(mockParam.getTodoId())).willReturn(preDataEntity);
		given(todoApiService.isChkSelfRef(mockParam.getTodoId(), null)).willReturn(new ResData<>(true, "성공했습니다."));
		given(todoApiService.modify(mockParam.getTodoId(), inputDataEntity.getContent(), inputDataEntity.getStatusCd(),
			mockParam.getRefTodos())).willReturn(new ResData<>(true, "성공"));

		log.info("test : {}", OM.writeValueAsString(mockParam));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            put("/api/v1/todo/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(OM.writeValueAsString(mockParam)))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
			.andDo(print())
            ;
        //@formatter:on

	}

	/**
	 * 수정 성공 테스트
	 * TODO : 추가해야함.
	 *
	 * @throws Exception
	 */
	@Test
	public void testSuccessModifyWithRef() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .content("집안일2")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .refTodos(new Long[]{1L, 2L})
            .build();

		//@formatter:on

		List<TodoRefEntity> refEntities = new ArrayList<>();
		for (Long refTodo : mockParam.getRefTodos()) {
			refEntities.add(TodoRefEntity.builder().parentTodoId(mockParam.getTodoId()).refTodoId(refTodo).build());
		}

		//@formatter:off
        TodoEntity preDataEntity = TodoEntity
			.builder()
				.content("집안일")
				.statusCd(TodoStatusCd.NOT_YET.name())
				.todoRefs(refEntities)
			.build();

        TodoEntity inputDataEntity = TodoEntity
			.builder()
				.todoId(mockParam.getTodoId())
				.content(mockParam.getContent())
				.statusCd(mockParam.getStatusCd())
			.build();
        //@formatter:on

		//given()

		given(todoApiService.get(mockParam.getTodoId())).willReturn(preDataEntity);
		given(todoApiService.isChkSelfRef(mockParam.getTodoId(), mockParam.getRefTodos())).willReturn(new ResData<>(true, "성공했습니다."));
		given(todoApiService.modify(mockParam.getTodoId(), inputDataEntity.getContent(), inputDataEntity.getStatusCd(),
			mockParam.getRefTodos())).willReturn(new ResData<>(true, "성공"));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            put("/api/v1/todo/{id}", mockParam.getTodoId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(OM.writeValueAsString(mockParam)))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

	}

}

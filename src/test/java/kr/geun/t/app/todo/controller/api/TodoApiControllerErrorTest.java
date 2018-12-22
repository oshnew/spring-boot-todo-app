package kr.geun.t.app.todo.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.geun.t.app.config.GlobalExceptionHandler;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 추가 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoApiController.class)
public class TodoApiControllerErrorTest {

	private MockMvc mvc;

	@MockBean
	private TodoApiService todoApiService;

	@InjectMocks
	private TodoApiController mockController;

	@Before
	public void setUp() {
		//@formatter:off
		mvc = MockMvcBuilders
			.standaloneSetup(mockController)
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();
		//@formatter:on
	}

	/**
	 * 시스템 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailSystemErrAdd() throws Exception {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Add mockParam = TodoDTO.Add.builder()
            .content("집안일")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .refTodos(new Long[] {1L, 2L})
			.build();

		//@formatter:on
		given(todoApiService.add(mockParam.getContent(), mockParam.getRefTodos())).willThrow(new RuntimeException("mock exception"));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            post("/api/v1/todo")
                .param("content", mockParam.getContent())
                .param("statusCd", mockParam.getStatusCd())
                .param("refTodos", "1","2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
	}

	/**
	 * 시스템 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailSystemErrGet() throws Exception {
		//GIVEN(Preparation)
		final Long mockTodoId = 1L;

		given(todoApiService.get(mockTodoId)).willThrow(new RuntimeException("mock exception"));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/{id}",mockTodoId)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
	}

	/**
	 * 시스템 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailSystemErrModify() throws Exception {
		ObjectMapper OM = new ObjectMapper();
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Modify mockParam = TodoDTO.Modify.builder()
            .todoId(1L)
            .content("집안일")
            .statusCd(TodoStatusCd.NOT_YET.name())
            .refTodos(new Long[]{1L, 2L})
            .build();
		//@formatter:on

		given(todoApiService.preModify(mockParam)).willThrow(new RuntimeException("mock exception"));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            put("/api/v1/todo/{id}", mockParam.getTodoId())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(OM.writeValueAsString(mockParam)))

            //THEN(Verification)
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
	}

	/**
	 * 시스템 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailSystemErrList() throws Exception {
		//GIVEN(Preparation)
		Sort sort = new Sort(Sort.Direction.DESC, "todoId");
		Pageable pageable = new PageRequest(0, 3, sort);

		given(todoApiService.list(pageable)).willThrow(new RuntimeException("mock exception"));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo")
                .param("page", "0")
                .param("size", "0")
                .param("sort", "todoId,DESC")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
	}

	/**
	 * 시스템 에러 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testFailMethodNotSupported() throws Exception {
		//GIVEN(Preparation)

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            post("/api/v1/todo/search")
                .param("keyword", "테스트")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isMethodNotAllowed())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
	}
}

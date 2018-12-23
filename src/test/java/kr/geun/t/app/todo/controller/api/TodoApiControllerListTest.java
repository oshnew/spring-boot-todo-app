package kr.geun.t.app.todo.controller.api;

import kr.geun.t.app.common.constants.CmnConst;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 목록 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class})
public class TodoApiControllerListTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TodoApiService todoApiService;

	@Test
	public void 리스트_파라미터_에러() throws Exception {
		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo")
				.param("pageNumber", "")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;

        //@formatter:on
	}

	/**
	 * 성공 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void 리스트가_비어있음() throws Exception {

		final int pageNumber = 0;
		Sort sort = new Sort(Sort.Direction.DESC, CmnConst.TODO_ID_STR);
		Pageable pageable = new PageRequest(pageNumber, CmnConst.RECORD_PER_COUNT, sort); //TODO : 상수값으로 변환해야함.

		given(todoApiService.list(pageable)).willReturn(new PageImpl<>(new ArrayList<>()));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo")
                .param("pageNumber", String.valueOf(pageable.getPageNumber()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;

        //@formatter:on
	}

	/**
	 * 성공 테스트
	 *
	 * @throws Exception
	 */
	@Test
	public void testSuccessList() throws Exception {

		final int pageNumber = 0;
		Sort sort = new Sort(Sort.Direction.DESC, CmnConst.TODO_ID_STR);
		Pageable pageable = new PageRequest(pageNumber, CmnConst.RECORD_PER_COUNT, sort); //TODO : 상수값으로 변환해야함.

		LocalDateTime ldt = LocalDateTime.now();
		List<TodoEntity> list = new ArrayList<>();

		list.add(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
		list.add(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
		list.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).createdAt(ldt).updatedAt(ldt).build());
		list.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());

		given(todoApiService.list(pageable)).willReturn(new PageImpl<>(list));

		//@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo")
                .param("pageNumber", String.valueOf(pageable.getPageNumber()))
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            .andExpect(jsonPath("$.data.pagination").isNotEmpty())
            .andExpect(jsonPath("$.data.resultList").isNotEmpty())
            .andExpect(jsonPath("$.data.resultList[0].content").isNotEmpty())
            .andExpect(jsonPath("$.data.resultList[0].createdAt").isNotEmpty())
            .andExpect(jsonPath("$.data.resultList[0].updatedAt").isNotEmpty())
            ;

        //@formatter:on
	}
}

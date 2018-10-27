package kr.geun.t.app.todo.controller.api;

import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 검색 컨트롤러 관련 test case
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class, TodoApiService.class})
public class TodoApiControllerSearchTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRefRepository todoRefRepository;

    @MockBean
    private TodoRepository todoRepository;

    /**
     * 조회 성공 테스트
     */
    @Test
    public void testFailSearch() throws Exception {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Search dbParam = TodoDTO.Search.builder()
            .keyword("청")
			.build();

		//@formatter:on

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/search")
                .param("keyword",dbParam.getKeyword())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;

        //@formatter:on
    }

    /**
     * 조회 성공 테스트
     */
    @Test
    public void testSuccessSearch() throws Exception {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Search dbParam = TodoDTO.Search.builder()
            .keyword("청소")
			.build();

		//@formatter:on

        List<TodoEntity> list = new ArrayList<>();

        list.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).build());
        list.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).build());

        given(todoRepository.findByContentContaining(dbParam.getKeyword())).willReturn(list);

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/search")
                .param("keyword",dbParam.getKeyword())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            .andExpect(jsonPath("$.data").isNotEmpty())
            ;

        //@formatter:on
    }
}

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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 단건조회 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class, TodoApiService.class})
public class TodoApiControllerGetTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRefRepository todoRefRepository;

    @MockBean
    private TodoRepository todoRepository;

    /**
     * 파라미터 에러 테스트
     *
     * @throws Exception
     */
    @Test
    public void testFailParameterErr() throws Exception {
        //GIVEN(Preparation)

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/{id}",0)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

    }

    /**
     * 파라미터 에러 테스트
     *  - 문자열을 넣었을 경우 에러발생 확인
     *
     * @throws Exception
     */
    @Test
    public void testFailParameterStringErr() throws Exception {
        //GIVEN(Preparation)

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/{id}","test")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").value("todoId is type Mismatch"))
            ;
        //@formatter:on

    }

    /**
     * 없는 데이터 조회 테스트
     *
     * @throws Exception
     */
    @Test
    public void testFailParameterNotFound() throws Exception {

        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get dbParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();

		given(todoRepository.findOne(dbParam.getTodoId())).willReturn(null);
		//@formatter:on

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on
    }

    /**
     * 단건조회 성공 테스트
     *
     * @throws Exception
     */
    @Test
    public void testSuccessGet() throws Exception {
        //GIVEN(Preparation)
        //@formatter:off
		TodoDTO.Get dbParam = TodoDTO.Get.builder()
            .todoId(1L)
			.build();

		given(todoRepository.findOne(dbParam.getTodoId())).willReturn(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build());
		//@formatter:on

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            get("/api/v1/todo/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.content").value("집안일"))
            .andExpect(jsonPath("$.data.statusCd").value(TodoStatusCd.NOT_YET.name()))
            ;
        //@formatter:on

    }

}

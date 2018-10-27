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

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 추가 컨트롤러 테스트
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = {TodoApiController.class, TodoApiService.class})
public class TodoApiControllerAddTest {

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
            post("/api/v1/todo")
                .param("content", "집안일")
                .param("refTodos", "1","2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.msg").isNotEmpty())
            ;
        //@formatter:on

    }

    /**
     * 추가 성공 테스트
     *
     * @throws Exception
     */
    @Test
    public void testSuccessAdd() throws Exception {
        //GIVEN(Preparation)
        LocalDateTime ldt = LocalDateTime.now();

        //@formatter:off
        TodoDTO.Add dbParam = TodoDTO.Add.builder()
			.content("집안일")
			.statusCd(TodoStatusCd.NOT_YET.name())
			.build();

        TodoEntity mockParam = TodoEntity.builder()
            .content(dbParam.getContent())
            .statusCd(dbParam.getStatusCd())
            .build();

        TodoEntity mockTodoEntity = TodoEntity.builder()
            .todoId(1L)
            .content(mockParam.getContent())
            .statusCd(mockParam.getStatusCd())
            .createdAt(ldt)
            .updatedAt(ldt)
                .build();


		//@formatter:on

        given(todoRepository.save(mockParam)).willReturn(mockTodoEntity);
        given(todoRepository.findOne(mockTodoEntity.getTodoId())).willReturn(mockTodoEntity);

        //@formatter:off
        mvc.perform(
            //WHEN(Execution)
            post("/api/v1/todo")
                .param("content", dbParam.getContent())
                .param("statusCd", dbParam.getStatusCd())

                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

            //THEN(Verification)
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.data.content").value(dbParam.getContent()))
            .andExpect(jsonPath("$.data.statusCd").value(dbParam.getStatusCd()))
            .andExpect(jsonPath("$.data.todoRefs").isEmpty())
            ;
        //@formatter:on

    }
}

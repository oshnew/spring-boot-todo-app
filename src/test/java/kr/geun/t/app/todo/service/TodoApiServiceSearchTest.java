package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.config.EhCacheConfig;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * 검색관련 조회 Test Case
 */
@Slf4j
@RunWith(SpringRunner.class)
@Import(EhCacheConfig.class)
@SpringBootTest
public class TodoApiServiceSearchTest {

	@Autowired
	private TodoApiService todoApiService;

	@MockBean
	private TodoRepository todoRepository;

	/**
	 * 조회 성공 테스트
	 */
	@Test
	public void testSuccessList() {
		//GIVEN(Preparation)
		//@formatter:off
		TodoDTO.Search mockParam = TodoDTO.Search.builder()
            .keyword("청소")
			.build();

		//@formatter:on

		List<TodoEntity> mockList = new ArrayList<>();

		mockList.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).build());
		mockList.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).build());

        given(todoRepository.findByContentStartingWith(mockParam.getKeyword())).willReturn(mockList);

		//WHEN(Execution)
		ResponseEntity<ResData<List<TodoEntity>>> result = todoApiService.search(mockParam);
		ResData<List<TodoEntity>> resultBody = result.getBody();
		List<TodoEntity> todoList = resultBody.getData();

		//THEN(Verification)
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(2, todoList.size()); //결과 리스트
	}
}

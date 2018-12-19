package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.constants.CmnConst;
import kr.geun.t.app.config.EhCacheConfig;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

/**
 * 목록조회 관련 Test case
 *
 * @author 김형근
 */
@Slf4j
@RunWith(SpringRunner.class)
@Import(EhCacheConfig.class)
@SpringBootTest
public class TodoApiServiceListTest {

	@Autowired
	private TodoApiService todoApiService;

	@MockBean
	private TodoRepository todoRepository;

	/**
	 * 리스트 조회 성공 테스트
	 * - 페이징 포함여부도 확인
	 */
	@Test
	public void testSuccessList() {
		//GIVEN(Preparation)
		LocalDateTime ldt = LocalDateTime.now();
		List<TodoEntity> mockList = new ArrayList<>();

		mockList.add(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
		mockList.add(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
		mockList.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).createdAt(ldt).updatedAt(ldt).build());
		mockList.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());

		Pageable pageable = new PageRequest(0, CmnConst.RECORD_PER_COUNT);
		given(todoRepository.findAll(pageable)).willReturn(new PageImpl<>(mockList));

		//WHEN(Execution)
		Page<TodoEntity> page = todoApiService.list(pageable);

		//THEN
		assertNotNull(page);
		assertEquals(page.getTotalElements(), mockList.size());
	}

	/**
	 * 빈 리스트 조회
	 */
	@Test
	public void testSuccessEmptyList() {
		//GIVEN(Preparation)
		Pageable pageable = new PageRequest(0, CmnConst.RECORD_PER_COUNT);
		given(todoRepository.findAll(pageable)).willReturn(new PageImpl<>(new ArrayList<>()));

		//WHEN(Execution)
		Page<TodoEntity> page = todoApiService.list(pageable);
		assertEquals(page.getTotalElements(), 0);
	}
}

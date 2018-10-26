package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

/**
 * 목록조회 관련 Test case
 *
 * @author 김형근
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApiServiceListTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoApiService todoApiService;

    @MockBean
    private TodoRepository mockTodoRepository;

    /**
     * 작성일, 최종수정일, 내용 포함여부
     */
    @Test
    public void testSuccessContainValues() {
        //GIVEN(Preparation)
        LocalDateTime ldt = LocalDateTime.now();
        List<TodoEntity> list = new ArrayList<>();

        list.add(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());

        Pageable pageable = new PageRequest(0, 3);
        given(mockTodoRepository.findAll(pageable)).willReturn(new PageImpl<>(list));

        //WHEN(Execution)
        Page<TodoEntity> result = todoRepository.findAll(pageable);

        //THEN(Verification)
        assertNotNull(result.getContent().get(0).getContent());
        assertNotNull(result.getContent().get(0).getCreatedAt());
        assertNotNull(result.getContent().get(0).getUpdatedAt());
    }

    /**
     * 리스트 조회 성공 테스트
     * - 페이징 포함여부도 확인
     */
    @Test
    public void testSuccessList() {
        //GIVEN(Preparation)
        LocalDateTime ldt = LocalDateTime.now();
        List<TodoEntity> list = new ArrayList<>();

        list.add(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).createdAt(ldt).updatedAt(ldt).build());
        list.add(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).createdAt(ldt).updatedAt(ldt).build());

        Pageable pageable = new PageRequest(0, 3);
        given(mockTodoRepository.findAll(pageable)).willReturn(new PageImpl<>(list));

        //WHEN(Execution)
        ResponseEntity<ResData<Map<String, Object>>> result = todoApiService.list(pageable);
        ResData<Map<String, Object>> resultBody = result.getBody();
        Map<String, Object> todoMap = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(todoMap.containsKey("resultList")); //결과 리스트
        assertTrue(todoMap.containsKey("pagination")); //페이지 체크
    }

    /**
     * 빈 리스트 조회
     */
    @Test
    public void testSuccessEmptyList() {
        //GIVEN(Preparation)
        Pageable pageable = new PageRequest(0, 3);
        given(mockTodoRepository.findAll(pageable)).willReturn(new PageImpl<>(new ArrayList<>()));

        //WHEN(Execution)
        ResponseEntity<ResData<Map<String, Object>>> result = todoApiService.list(pageable);
        ResData<Map<String, Object>> resultBody = result.getBody();
        Map<String, Object> todoMap = resultBody.getData();

        //THEN(Verification)
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(todoMap);
        assertTrue(todoMap.isEmpty());
    }
}

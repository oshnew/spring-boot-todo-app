package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 할일 관련 API 서비스
 *
 * @author akageun
 */
public interface TodoApiService {

	/**
	 * 목록조회
	 *
	 * @param pageable
	 * @return
	 */
	Page<TodoEntity> list(Pageable pageable);

	/**
	 * 단건조회
	 *
	 * @param todoId
	 * @return
	 */
	TodoEntity get(Long todoId);

	/**
	 * 추가
	 *
	 * @param content
	 * @param refTodos
	 * @return
	 */
	TodoEntity add(String content, Long[] refTodos);

	/**
	 * 수정
	 *  - 전처리
	 *
	 * @param param
	 * @return
	 */
	ResponseEntity<ResData<TodoEntity>> preModify(TodoDTO.Modify param);

	/**
	 * 수정
	 *
	 * @param param
	 * @return
	 */
	ResponseEntity<ResData<TodoEntity>> modify(TodoDTO.Modify param);

	/**
	 * 완료처리
	 * - 전처리
	 *
	 * @param param
	 * @return
	 */
	ResponseEntity<ResData<TodoEntity>> preModifyStatus(TodoDTO.ModifyStatus param);

	/**
	 * 완료처리
	 *
	 * @param param
	 * @return
	 */
	ResponseEntity<ResData<TodoEntity>> modifyStatus(TodoDTO.ModifyStatus param);

	/**
	 * 검색
	 *
	 * @param keyword
	 * @return
	 */
	List<TodoEntity> search(String keyword);
}

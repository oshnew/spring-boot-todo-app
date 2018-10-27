package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Map;

/**
 * 할일 관련 API 서비스
 *
 * @author akageun
 */
public interface TodoApiService {

    /**
     * 리스트
     *
     * @param pageable
     * @return
     */
    ResponseEntity<ResData<Map<String, Object>>> list(Pageable pageable);

    /**
     * 단건조회
     *
     * @param param
     * @return
     */
    ResponseEntity<ResData<TodoEntity>> get(TodoDTO.Get param);

    /**
     * 추가
     *
     * @param param
     * @return
     */
    ResponseEntity<ResData<TodoEntity>> add(TodoDTO.Add param);

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
}

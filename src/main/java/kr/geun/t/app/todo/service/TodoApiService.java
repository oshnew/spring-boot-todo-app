package kr.geun.t.app.todo.service;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import org.springframework.http.ResponseEntity;

/**
 * 할일 관련 API 서비스
 *
 * @author akageun
 */
public interface TodoApiService {

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
}

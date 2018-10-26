package kr.geun.t.app.todo.service.impl;

import kr.geun.t.app.common.pagination.PaginationInfo;
import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRefRepository;
import kr.geun.t.app.todo.repository.TodoRepository;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 할일 관련 API 서비스
 *
 * @author akageun
 */
@Slf4j
@Service
public class TodoApiServiceImpl implements TodoApiService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoRefRepository todoRefRepository;

    /**
     * 리스트
     *
     * @param pageable
     * @return
     */
    @Override
    public ResponseEntity<ResData<Map<String, Object>>> list(Pageable pageable) {
        Map<String, Object> rtnMap = new HashMap<>();

        Page<TodoEntity> resultList = todoRepository.findAll(pageable);
        if (resultList.getTotalPages() == 0) {
            return new ResponseEntity<>(new ResData<>(rtnMap, "등록된 데이터가 없습니다."), HttpStatus.OK);
        }

        //@formatter:off
        PaginationInfo paginationInfo = new PaginationInfo(
            resultList.getNumber(),
            resultList.getNumberOfElements(),
            resultList.getTotalElements(),
			resultList.getTotalPages(),
            pageable.getPageSize());
        //@formatter:on

        rtnMap.put("resultList", resultList);
        rtnMap.put("pagination", paginationInfo);

        return new ResponseEntity<>(new ResData<>(rtnMap, "성공했습니다."), HttpStatus.OK);
    }

    /**
     * 단건조회
     *
     * @param param
     * @return
     */
    @Override
    public ResponseEntity<ResData<TodoEntity>> get(TodoDTO.Get param) {
        TodoEntity dbInfo = todoRepository.findOne(param.getTodoId());
        if (dbInfo == null) {
            return new ResponseEntity<>(new ResData<>(null, "데이터를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResData<>(dbInfo, "성공했습니다."), HttpStatus.OK);
    }

    /**
     * 추가
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public ResponseEntity<ResData<TodoEntity>> add(TodoDTO.Add param) {
        //@formatter:off
        TodoEntity dbParam = TodoEntity
            .builder()
                .content(param.getContent())
                .statusCd(param.getStatusCd())
            .build();
        //@formatter:on

        TodoEntity dbInfo = todoRepository.save(dbParam);

        if (param.getRefTodos() != null && param.getRefTodos().length > 0) { //참조걸린 할일들
            List<TodoRefEntity> refEntities = new ArrayList<>();

            for (Long todoId : param.getRefTodos()) {
                //@formatter:off
                TodoRefEntity refParam = TodoRefEntity
                    .builder()
                        .parentTodoId(dbInfo.getTodoId())
                        .refTodoId(todoId)
                    .build();
                //@formatter:on

                refEntities.add(refParam);
            }

            todoRefRepository.save(refEntities);
        }

        return new ResponseEntity<>(new ResData<>(dbInfo, "성공했습니다."), HttpStatus.CREATED);
    }

    /**
     * 수정
     *  - 전처리
     *
     * @param param
     * @return
     */
    @Override
    public ResponseEntity<ResData<TodoEntity>> preModify(TodoDTO.Modify param) {
        TodoEntity dbInfo = todoRepository.findOne(param.getTodoId());
        if (dbInfo == null) {
            return new ResponseEntity<>(new ResData<>(null, "데이터를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new ResData<>(dbInfo, "성공했습니다."), HttpStatus.OK);
    }

    /**
     * 수정
     *
     * @param param
     * @return
     */
    @Transactional
    @Override
    public ResponseEntity<ResData<TodoEntity>> modify(TodoDTO.Modify param) {
        //@formatter:off
		TodoEntity dbParam = TodoEntity.builder()
			.todoId(param.getTodoId())
			.content(param.getContent())
			.statusCd(param.getStatusCd())
				.build();
		//@formatter:on

        TodoEntity dbInfo = todoRepository.save(dbParam);

        return new ResponseEntity<>(new ResData<>(dbInfo, "성공했습니다."), HttpStatus.OK);
    }
}

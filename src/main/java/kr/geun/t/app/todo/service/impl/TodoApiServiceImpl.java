package kr.geun.t.app.todo.service.impl;

import kr.geun.t.app.common.pagination.PaginationInfo;
import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.todo.code.TodoStatusCd;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.entity.TodoRefEntity;
import kr.geun.t.app.todo.repository.TodoRefRepository;
import kr.geun.t.app.todo.repository.TodoRepository;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        if (resultList.getTotalElements() == 0) {
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

        TodoEntity dbTmpInfo = todoRepository.save(dbParam);

        if (param.getRefTodos() != null && param.getRefTodos().length > 0) { //참조걸린 할일들
            List<TodoRefEntity> refEntities = new ArrayList<>();

            for (Long todoId : param.getRefTodos()) {
                //@formatter:off
                TodoRefEntity refParam = TodoRefEntity
                    .builder()
                        .parentTodoId(dbTmpInfo.getTodoId())
                        .refTodoId(todoId)
                    .build();
                //@formatter:on

                refEntities.add(refParam);
            }

            todoRefRepository.save(refEntities);
        }

        TodoEntity dbInfo = todoRepository.findOne(dbTmpInfo.getTodoId());

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

        return new ResponseEntity<>(new ResData<>("성공했습니다."), HttpStatus.OK);
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

    /**
     * 완료처리
     * - 전처리
     *
     * @param param
     * @return
     */
    @Override
    public ResponseEntity<ResData<TodoEntity>> preModifyStatus(TodoDTO.ModifyStatus param) {
        TodoEntity dbInfo = todoRepository.findOne(param.getTodoId());
        if (dbInfo == null) {
            return new ResponseEntity<>(new ResData<>("데이터를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
        }

        boolean refsCmplExist = false;
        if (dbInfo.getTodoRefs() != null && dbInfo.getTodoRefs().isEmpty() == false) {
            refsCmplExist = dbInfo.getTodoRefs().stream().anyMatch(
                t -> StringUtils.equals(t.getTodoRefsInfo().getStatusCd(), TodoStatusCd.NOT_YET.name()));
        }

        if (refsCmplExist) {
            return new ResponseEntity<>(new ResData<>("참조한 이슈들 중 완료되지 않은 이슈가 있습니다."), HttpStatus.BAD_REQUEST);
        }

        param.setContent(dbInfo.getContent()); // TODO : 다이나믹 쿼리가 되면 제거 예정

        return new ResponseEntity<>(new ResData<>("성공했습니다."), HttpStatus.OK);
    }

    /**
     * 완료처리
     *
     * @param param
     * @return
     */
    @Override
    public ResponseEntity<ResData<TodoEntity>> modifyStatus(TodoDTO.ModifyStatus param) {

        //@formatter:off
		TodoEntity dbParam = TodoEntity.builder()
			.todoId(param.getTodoId())
			.statusCd(param.getStatusCd())
				.build();

		dbParam.setContent(param.getContent()); // TODO : 다이나믹 쿼리가 되면 제거 예정
		//@formatter:on

        todoRepository.save(dbParam);

        return new ResponseEntity<>(new ResData<>("성공했습니다."), HttpStatus.OK);
    }
}

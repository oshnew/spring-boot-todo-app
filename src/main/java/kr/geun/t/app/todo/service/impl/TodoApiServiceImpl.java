package kr.geun.t.app.todo.service.impl;

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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	 * 목록조회
	 *
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<TodoEntity> list(Pageable pageable) {
		return todoRepository.findAll(pageable);
	}

	/**
	 * 단건조회
	 *
	 * @param todoId
	 * @return
	 */
	@Override
	public TodoEntity get(Long todoId) {
		return todoRepository.findOne(todoId);
	}

	/**
	 * 추가
	 *
	 * @param content
	 * @param refTodos
	 * @return
	 */
	@Transactional
	@Override
	public TodoEntity add(String content, Long[] refTodos) {
		//@formatter:off
        TodoEntity dbParam = TodoEntity
            .builder()
                .content(content)
                .statusCd(TodoStatusCd.NOT_YET.name())
            .build();
        //@formatter:on

		TodoEntity dbTmpInfo = todoRepository.save(dbParam);
		addTodoRefs(refTodos, dbTmpInfo.getTodoId());

		return dbTmpInfo;
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
			return new ResponseEntity<>(new ResData<>("데이터를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
		}

		param.setStatusCd(dbInfo.getStatusCd());

		if (param.getRefTodos() != null && param.getRefTodos().length > 0) { //참조걸린 할일들
			List<Long> tt = Arrays.asList(param.getRefTodos());

			if (tt.contains(dbInfo.getTodoId())) {
				return new ResponseEntity<>(new ResData<>("자기 자신을 참조할 수 없습니다."), HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(new ResData<>("성공했습니다."), HttpStatus.OK);
	}

	/**
	 * 본인 스스로를 참조하는지 체크
	 *
	 * @param todoId
	 * @param refTodos
	 * @return
	 */
	@Override
	public ResData<TodoEntity> isChkSelfRef(Long todoId, Long[] refTodos) {
		if (refTodos != null && refTodos.length > 0) { //참조걸린 할일들
			List<Long> tt = Arrays.asList(refTodos);

			if (tt.contains(todoId)) {
				return new ResData<>(false, "자기 자신을 참조할 수 없습니다.");
			}
		}

		return new ResData<>(true, "성공했습니다.");
	}

	/**
	 * 글 수정
	 *
	 * @param todoId
	 * @param content
	 * @param statusCd
	 * @param refTodos
	 * @return
	 */
	@Transactional
	@Override
	public ResData<TodoEntity> modify(Long todoId, String content, String statusCd, Long[] refTodos) {
		//@formatter:off
		TodoEntity dbParam = TodoEntity.builder()
			.todoId(todoId)
			.content(content)
			.statusCd(statusCd)
				.build();
		//@formatter:on

		TodoEntity dbInfo = todoRepository.save(dbParam);
		todoRefRepository.deleteByParentTodoId(dbInfo.getTodoId());

		addTodoRefs(refTodos, todoId);

		return new ResData<>(true, "성공했습니다.");
	}

	/**
	 * 참조 할일을 추가
	 *
	 * @param refTodos
	 * @param todoId
	 */
	private void addTodoRefs(Long[] refTodos, long todoId) {
		if (refTodos != null && refTodos.length > 0) { //참조걸린 할일들
			List<TodoRefEntity> refEntities = new ArrayList<>();

			for (Long refTodoId : refTodos) {
				//@formatter:off
                TodoRefEntity refParam = TodoRefEntity
                    .builder()
                        .parentTodoId(todoId)
                        .refTodoId(refTodoId)
                    .build();
                //@formatter:on

				refEntities.add(refParam);
			}

			todoRefRepository.save(refEntities);
		}
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

		List<TodoRefEntity> refEntityList = todoRefRepository.findByRefTodoId(param.getTodoId());

		boolean refsCmplExist = false;
		if (refEntityList.isEmpty() == false) {
			refsCmplExist = refEntityList.stream().anyMatch(t -> StringUtils.equals(t.getTodoJoinInfo().getStatusCd(), TodoStatusCd.NOT_YET.name()));
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
	@Transactional
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

	/**
	 * 검색
	 * - TTL 을 짧게 설정하여 반복적인 쿼리 요청 방어
	 * - 기존 양방향 검색에서 후방 검색만 추가하여 Index 적용
	 *
	 * @param keyword
	 * @return
	 */
	@Override
	@Cacheable(cacheNames = "searchApiCache", key = "#keyword")
	public List<TodoEntity> search(String keyword) {
		return todoRepository.findByContentStartingWith(keyword);
	}
}

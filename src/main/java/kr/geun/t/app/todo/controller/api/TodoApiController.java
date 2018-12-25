package kr.geun.t.app.todo.controller.api;

import kr.geun.t.app.common.constants.CmnConst;
import kr.geun.t.app.common.pagination.PaginationInfo;
import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.common.utils.CmnUtils;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 할일 관련 API
 *
 * @author akageun
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class TodoApiController {

	@Autowired
	private TodoApiService todoApiService;

	/**
	 * 리스트
	 *
	 * @param param
	 * @param result
	 * @return
	 */
	@GetMapping(value = "/todo")
	public ResponseEntity<ResData<Map<String, Object>>> list(@Valid TodoDTO.List param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		Sort sort = new Sort(Sort.Direction.DESC, CmnConst.TODO_ID_STR);
		Pageable pageable = new PageRequest(param.getPageNumber(), CmnConst.RECORD_PER_COUNT, sort);

		Page<TodoEntity> pageInfo = todoApiService.list(pageable);

		if (pageInfo.getTotalElements() == 0) {
			return ResponseEntity.ok(new ResData<>("등록된 데이터가 없습니다."));
		}

		//@formatter:off
        PaginationInfo paginationInfo = new PaginationInfo(
            pageInfo.getNumber(),
            pageInfo.getNumberOfElements(),
            pageInfo.getTotalElements(),
			pageInfo.getTotalPages(),
            pageable.getPageSize());
        //@formatter:on

		Map<String, Object> rtnMap = new HashMap<>();

		rtnMap.put("resultList", pageInfo.getContent());
		rtnMap.put("pagination", paginationInfo);

		return ResponseEntity.ok(new ResData<>(true, rtnMap, "성공했습니다."));
	}

	/**
	 * 단건조회
	 *
	 * @param param
	 * @param result
	 * @return
	 */

	@GetMapping(value = "/todo/{todoId}")
	public ResponseEntity<ResData<TodoEntity>> get(@Valid TodoDTO.Get param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		TodoEntity dbInfo = todoApiService.get(param.getTodoId());
		if (dbInfo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResData<>("데이터를 찾을 수 없습니다."));
		}

		return ResponseEntity.ok(new ResData<>(true, dbInfo, "성공했습니다."));

	}

	/**
	 * 추가
	 *
	 * @param param
	 * @param result
	 * @return
	 */
	@PostMapping("/todo")
	public ResponseEntity<ResData<TodoEntity>> add(@ModelAttribute @Valid TodoDTO.Add param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		todoApiService.add(param.getContent(), param.getRefTodos());

		return ResponseEntity.status(HttpStatus.CREATED).body(new ResData<>("성공했습니다."));
	}

	/**
	 * 수정
	 *
	 * @param param
	 * @param result
	 * @return
	 */
	@PutMapping("/todo/{todoId}")
	public ResponseEntity<ResData<TodoEntity>> modify(@RequestBody @Valid TodoDTO.Modify param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		TodoEntity dbInfo = todoApiService.get(param.getTodoId());
		if (dbInfo == null) {
			return new ResponseEntity<>(new ResData<>("데이터를 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
		}

		ResData<TodoEntity> preCheck = todoApiService.isChkSelfRef(param.getTodoId(), param.getRefTodos());
		if (preCheck.getResult() == false) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(preCheck);
		}

		todoApiService.modify(param.getTodoId(), param.getContent(), dbInfo.getStatusCd(), param.getRefTodos());

		return ResponseEntity.ok(new ResData<>("성공했습니다."));

	}

	/**
	 * 상태값 수정
	 *
	 * @param param
	 * @param result
	 * @return
	 */
	@PatchMapping("/todo/{todoId}")
	public ResponseEntity<ResData<TodoEntity>> status(@RequestBody @Valid TodoDTO.ModifyStatus param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		ResponseEntity<ResData<TodoEntity>> preChk = todoApiService.preModifyStatus(param);
		if (preChk.getStatusCode().is2xxSuccessful() == false) {
			return preChk;
		}

		return todoApiService.modifyStatus(param);

	}

	/**
	 * 검색
	 *
	 * @param param
	 * @param result
	 * @return
	 */
	@GetMapping(value = "/todo/search")
	public ResponseEntity<ResData<List<TodoEntity>>> search(@Valid TodoDTO.Search param, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResData<>(CmnUtils.getErrMsg(result, CmnConst.NEW_LINE)));
		}

		List<TodoEntity> searchList = todoApiService.search(param.getKeyword());

		return new ResponseEntity<>(new ResData<>(true, searchList, "성공했습니다."), HttpStatus.OK);
	}
}

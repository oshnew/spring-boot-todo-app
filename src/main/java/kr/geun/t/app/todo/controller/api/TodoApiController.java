package kr.geun.t.app.todo.controller.api;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.common.utils.CmnUtils;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 할일 관련 API
 *
 * @author akageun
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v1", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
            return new ResponseEntity<>(new ResData<>(CmnUtils.getErrMsg(result, "<br>")), HttpStatus.BAD_REQUEST);
        }

        Sort sort = new Sort(Sort.Direction.DESC, "todoId");
        Pageable pageable = new PageRequest(param.getPageNumber(), 3, sort);

        return todoApiService.list(pageable); //글로벌 익셉션 핸들링 필요
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
            return new ResponseEntity<>(new ResData<>(CmnUtils.getErrMsg(result, "<br>")), HttpStatus.BAD_REQUEST);
        }

        return todoApiService.get(param);

    }

    /**
     * 추가
     *
     * @param param
     * @param result
     * @return
     */
    @PostMapping("/todo")
    public ResponseEntity<ResData<TodoEntity>> add(@Valid TodoDTO.Add param, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(new ResData<>(CmnUtils.getErrMsg(result, "<br>")), HttpStatus.BAD_REQUEST);
        }

        return todoApiService.add(param);
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
            return new ResponseEntity<>(new ResData<>(CmnUtils.getErrMsg(result, "<br>")), HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<ResData<TodoEntity>> preChk = todoApiService.preModify(param);
        if (preChk.getStatusCode().is2xxSuccessful() == false) {
            return preChk;
        }

        return todoApiService.modify(param);

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
            return new ResponseEntity<>(new ResData<>(CmnUtils.getErrMsg(result, "<br>")), HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<ResData<TodoEntity>> preChk = todoApiService.preModifyStatus(param);
        if (preChk.getStatusCode().is2xxSuccessful() == false) {
            return preChk;
        }

        return todoApiService.modifyStatus(param);

    }
}

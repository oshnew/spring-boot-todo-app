package kr.geun.t.app.todo.controller.api;

import kr.geun.t.app.common.response.ResData;
import kr.geun.t.app.common.utils.CmnUtils;
import kr.geun.t.app.todo.dto.TodoDTO;
import kr.geun.t.app.todo.entity.TodoEntity;
import kr.geun.t.app.todo.service.TodoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

        try {
            return todoApiService.get(param);

        } catch (Exception e) {
            log.error("error message : {} || param : {} || {}", e.getMessage(), param, e);
            return new ResponseEntity<>(new ResData<>("시스템에러가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }

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

        try {
            return todoApiService.add(param);

        } catch (Exception e) {
            log.error("error message : {} || param : {} || {}", e.getMessage(), param, e);
            return new ResponseEntity<>(new ResData<>("시스템에러가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

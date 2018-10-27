package kr.geun.t.app.config;

import kr.geun.t.app.common.response.ResData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Exception 핸들링
 *
 * @author akageun
 */
@Slf4j
@ControllerAdvice(basePackages = "kr.geun.t.app.todo")
public class ExceptionHandler {

    /**
     * Exception 전체에 대한 핸들링
     *
     * @param e
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ResData> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new ResData<>("시스템에러가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 허용되지 않는 HTTP 메소드 요청시 에러 핸들러
     *
     * @param ex
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResData> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return new ResponseEntity<>(new ResData<>("Not allow your request"), HttpStatus.METHOD_NOT_ALLOWED);
    }

    /**
     * 유효하지 않은 메시지 포맷 예외처리 핸들러(ex. 유효하지 않은 Json포맷)
     *
     * @param ex
     * @return
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResData> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new ResData<>(String.format("HttpMessageNotReadable occurred. : {}", ex.getMessage())), HttpStatus.BAD_REQUEST);
    }
}

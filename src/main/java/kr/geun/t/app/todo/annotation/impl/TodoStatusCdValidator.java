package kr.geun.t.app.todo.annotation.impl;

import kr.geun.t.app.todo.annotation.TodoStatusCdValid;
import kr.geun.t.app.todo.code.TodoStatusCd;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 할일 상태값에 대한 Custom Valid 구현체
 *
 * @author akageun
 */
public class TodoStatusCdValidator implements ConstraintValidator<TodoStatusCdValid, String> {

    /**
     * 초기화
     *
     * @param constraintAnnotation
     */
    @Override
    public void initialize(TodoStatusCdValid constraintAnnotation) {

    }

    /**
     * 유효성 검사에 대한 구현 메소드
     *
     * @param value
     * @param context
     *
     * @return false
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        TodoStatusCd statusCd = EnumUtils.getEnum(TodoStatusCd.class, value);
        if (statusCd == null) {
            return false;
        }

        return true;
    }
}

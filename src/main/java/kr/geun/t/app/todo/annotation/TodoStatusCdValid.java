package kr.geun.t.app.todo.annotation;

import kr.geun.t.app.todo.annotation.impl.TodoStatusCdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 할일 상태값에 대한 Custom Valid
 *
 * @author akageun
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TodoStatusCdValidator.class)
public @interface TodoStatusCdValid {

    String message() default "상태값은 공백이거나, 지정되지 않은 상태값을 추가 할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package kr.geun.t.app.todo.dto;

import kr.geun.t.app.todo.annotation.TodoStatusCdValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 할일 DTO
 *
 * @author akageun
 */
public class TodoDTO {

    /**
     * 추가
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Add {
        @Size(min = 2, max = 1024)
        @NotEmpty
        private String content;

        @TodoStatusCdValid
        private String statusCd;

        private Long[] refTodos;
    }

    /**
     * 단건조회
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Get {

        @Min(0)
        private Long todoId;
    }
}

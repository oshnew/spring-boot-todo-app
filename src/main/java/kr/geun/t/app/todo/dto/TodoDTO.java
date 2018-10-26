package kr.geun.t.app.todo.dto;

import kr.geun.t.app.todo.annotation.TodoStatusCdValid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

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
        @NotBlank
        @Size(min = 2, max = 1024)
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

        @Min(1)
        private Long todoId;
    }

    /**
     * 수정
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modify {

        @Min(1)
        private Long todoId;

        @NotBlank
        @Size(min = 2, max = 1024)
        private String content;

        @TodoStatusCdValid
        private String statusCd;

        private Long[] refTodos;
    }
}

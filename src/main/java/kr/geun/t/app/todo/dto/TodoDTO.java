package kr.geun.t.app.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

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

        private String statusCd;

        private Long[] refTodos;
    }
}

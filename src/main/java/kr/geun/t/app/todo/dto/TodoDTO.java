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
	 * 리스트
	 */
	@Data
	@NoArgsConstructor
	public static class List {
		@Min(0)
		private int pageNumber;
	}

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

		private String statusCd;

		private Long[] refTodos;
	}

	/**
	 * 상태값 수정
	 */
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyStatus {
		@Min(1)
		private Long todoId;

		@TodoStatusCdValid
		private String statusCd;

		private String content; //TODO : 추후 다이나믹쿼리를 사용하면 제거
	}

	/**
	 * 검색
	 */
	@Data
	@NoArgsConstructor
	public static class Search {

		@NotBlank
		@Size(min = 2, max = 1024)
		private String keyword;
	}
}

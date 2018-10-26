package kr.geun.t.app.todo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 할일 관련 메인 테이블
 *
 * @author akageun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todo")
public class TodoEntity {

	/**
	 * 할일 PK
	 */
	@Id
	@Column(name = "todo_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long todoId;

	/**
	 * 할일 내용
	 */
	@Column(name = "content", length = 1024, nullable = false)
	private String content;

	/**
	 * 할일의 상태값
	 *  - EX) NOT_YET, COMPLETE 등...
	 */
	@Column(name = "status_cd", nullable = false)
	private String statusCd;

	/**
	 * 생성일시
	 */
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	/**
	 * 수정일시
	 */
	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
}

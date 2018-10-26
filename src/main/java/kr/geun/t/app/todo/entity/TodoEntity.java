package kr.geun.t.app.todo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

	@JsonManagedReference
	@OneToMany(mappedBy = "todoJoinInfo", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("ref_todo_id DESC")
	private List<TodoRefEntity> todoRefs;

	/**
	 * 무한루프로 인해 override
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}

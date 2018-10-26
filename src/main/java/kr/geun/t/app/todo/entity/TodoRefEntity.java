package kr.geun.t.app.todo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

/**
 * 할일 관련 참조 Entity
 *
 * @author akageun
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "todo_reference")
public class TodoRefEntity {

	/**
	 * 참조 할일 PK
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ref_id")
	private Long refId;

	/**
	 * 상위 할일 번호
	 */
	@Column(name = "parent_todo_id")
	private Long parentTodoId;

	/**
	 * 참조한 할일 번호
	 */
	@Column(name = "ref_todo_id")
	private Long refTodoId;

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

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "parent_todo_id", insertable = false, updatable = false)
	private TodoEntity todoJoinInfo;

	@OneToOne
	@JoinColumn(name = "ref_todo_id", referencedColumnName = "todo_id", insertable = false, updatable = false)
	@JsonIgnoreProperties("todoRefs") //무한루프 방지
	private TodoEntity todoRefsInfo;

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

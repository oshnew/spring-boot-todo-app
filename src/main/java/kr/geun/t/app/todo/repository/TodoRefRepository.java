package kr.geun.t.app.todo.repository;

import kr.geun.t.app.todo.entity.TodoRefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 할일 참조 관련 Repository
 *
 * @author akageun
 */
public interface TodoRefRepository extends JpaRepository<TodoRefEntity, Long> {

	/**
	 * 상위 할일 아이디로 삭제
	 *
	 * @param parentTodoId
	 */
	void deleteByParentTodoId(long parentTodoId);

	/**
	 * 자신을 참조한 할일 가져오기
	 *
	 * @param todoId
	 * @return
	 */
	List<TodoRefEntity> findByRefTodoId(Long todoId);

}

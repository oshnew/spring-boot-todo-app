package kr.geun.t.app.todo.repository;

import kr.geun.t.app.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 할일 관련 Repository
 *
 * @author akageun
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

	/**
	 * 내용 검색
	 *
	 * @param keyword
	 * @return
	 */
	List<TodoEntity> findByContentStartingWith(String keyword);
}

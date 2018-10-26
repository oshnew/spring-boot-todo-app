package kr.geun.t.app.todo.repository;

import kr.geun.t.app.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 할일 관련 Repository
 *
 * @author akageun
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}

package kr.geun.t.app.todo.repository;

import kr.geun.t.app.todo.entity.TodoRefEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 할일 참조 관련 Repository
 *
 * @author akageun
 */
public interface TodoRefRepository extends JpaRepository<TodoRefEntity, Long> {
}

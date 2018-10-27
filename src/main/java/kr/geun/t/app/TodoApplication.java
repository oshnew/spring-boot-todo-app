package kr.geun.t.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 프로젝트 Entry Class
 *
 * @author akageun
 */
@SpringBootApplication
public class TodoApplication /*extends CommandLineRunner*/ {

	/**
	 * Main Method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

	//	@Autowired
	//	private TodoRepository todoRepository;
	//
	//	@Autowired
	//	private TodoRefRepository todoRefRepository;

	//	@Override
	//	public void run(String... strings) throws Exception {
	//		TodoEntity t0 = todoRepository.save(TodoEntity.builder().content("집안일").statusCd(TodoStatusCd.NOT_YET.name()).build());
	//		TodoEntity t1 = todoRepository.save(TodoEntity.builder().content("빨래").statusCd(TodoStatusCd.NOT_YET.name()).build());
	//		TodoEntity t2 = todoRepository.save(TodoEntity.builder().content("청소").statusCd(TodoStatusCd.COMPLETE.name()).build());
	//		TodoEntity t3 = todoRepository.save(TodoEntity.builder().content("방청소").statusCd(TodoStatusCd.NOT_YET.name()).build());
	//
	//		todoRefRepository.save(TodoRefEntity.builder().parentTodoId(t1.getTodoId()).refTodoId(t0.getTodoId()).build());
	//		todoRefRepository.save(TodoRefEntity.builder().parentTodoId(t2.getTodoId()).refTodoId(t0.getTodoId()).build());
	//		todoRefRepository.save(TodoRefEntity.builder().parentTodoId(t3.getTodoId()).refTodoId(t0.getTodoId()).build());
	//		todoRefRepository.save(TodoRefEntity.builder().parentTodoId(t3.getTodoId()).refTodoId(t2.getTodoId()).build());
	//	}
}

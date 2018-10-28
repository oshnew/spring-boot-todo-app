package kr.geun.t.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 프로젝트 Entry Class
 *
 * @author akageun
 */
@SpringBootApplication
public class TodoApplication {

	/**
	 * Main Method
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}
}

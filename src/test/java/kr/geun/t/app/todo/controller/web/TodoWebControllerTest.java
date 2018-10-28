package kr.geun.t.app.todo.controller.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * 할일 웹페이지 컨트롤러
 *
 * @author akageun
 */
@Slf4j
@RunWith(SpringRunner.class)
@WebMvcTest(value = TodoWebController.class)
public class TodoWebControllerTest {

	@Autowired
	private MockMvc mvc;

	/**
	 * 할일 웹페이지
	 *
	 * @throws Exception
	 */
	@Test
	public void todoPage() throws Exception {
		//@formatter:off
		mvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"))
			;
		//@formatter:on
	}
}
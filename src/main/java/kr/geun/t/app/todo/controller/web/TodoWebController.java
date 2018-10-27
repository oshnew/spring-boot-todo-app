package kr.geun.t.app.todo.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 할일 웹페이지
 *
 * @author akageun
 */
@Controller
public class TodoWebController {

    /**
     * 할일 페이지
     *
     * @return
     */
    @GetMapping(value = {"", "/"})
    public String todoPage() {
        return "index";
    }
}

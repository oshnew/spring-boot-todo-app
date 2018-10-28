package kr.geun.t.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoApplicationTests {
	@Test
	public void contextLoads() {
		TodoApplication.main(new String[]{
			"--server.port=18080"
			// Override any other environment properties according to your needs
		});
	}

}

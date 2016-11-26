package com.wangab;

import com.wangab.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeStarServiceApplicationTests {

	@Test
	public void contextLoads() {
		System.out.printf(StringUtils.createId());
	}

}

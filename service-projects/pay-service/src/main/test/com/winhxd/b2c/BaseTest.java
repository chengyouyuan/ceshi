package com.winhxd.b2c;


import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.winhxd.b2c.pay.PayServiceApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayServiceApplication.class)
@Rollback(value = false)
public class BaseTest {

	public BaseTest() {
		super();
	}

}

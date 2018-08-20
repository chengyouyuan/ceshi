package com.winhxd.b2c.pay;

import com.winhxd.b2c.pay.weixin.base.config.PayConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ValueTest
 *
 * @Author yindanqing
 * @Date 2018/8/18 14:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ValueTest {

    @Autowired
    private PayConfig payConfig;

    @Test
    public void testValue(){
        System.out.println(payConfig.getAppID());
    }

}

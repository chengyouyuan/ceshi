package com.winhxd.b2c.system;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictItemCondition;
import com.winhxd.b2c.system.api.ApiSystemController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SystemServiceApplicationTest {

    @Autowired
    private ApiSystemController apiSystemController;

    @Test
    public void contextLoads() {
        SysDictItemCondition sysDictItemCondition = new SysDictItemCondition();
        sysDictItemCondition.setAppVersion("1.4.0");
        ResponseResult<Integer> integerResponseResult = apiSystemController.appSubmitCheckedVersion(sysDictItemCondition);
        System.out.print(integerResponseResult.getData());
    }
}

package com.winhxd.b2c.system;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.AppVersionCondition;
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
        AppVersionCondition appVersionCondition = new AppVersionCondition();
        appVersionCondition.setAppVersion("1.4.0");
        ResponseResult<Integer> integerResponseResult = apiSystemController.appSubmitCheckedVersion(appVersionCondition);
        System.out.print(integerResponseResult.getData());
    }
}

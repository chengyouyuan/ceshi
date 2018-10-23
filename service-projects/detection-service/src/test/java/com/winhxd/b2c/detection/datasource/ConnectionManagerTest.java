package com.winhxd.b2c.detection.datasource;

import com.winhxd.b2c.common.domain.common.inputmodel.DateInterval;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 * @Auther: Louis
 * @Date: 2018/10/19 16:55
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConnectionManagerTest {

    @Test
    public void testConnection() throws ParseException {
        DbSource source = new DbSource();
        source.setId(1L);
        source.setUserName("root");
        source.setPassword("root");
        source.setUri("jdbc:mysql://localhost:3306/retail2C?useSSL=false&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        Connection conn = ConnectionManager.getConnection(source);
        System.out.println(conn);
    }

}

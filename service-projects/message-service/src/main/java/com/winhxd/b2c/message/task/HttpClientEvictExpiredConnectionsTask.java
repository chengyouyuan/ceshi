package com.winhxd.b2c.message.task;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @Description: 针对httpclient池中的链接回收
 * @author chengyy
 * @date 2018/8/4 10:49
 */
@Component
public class HttpClientEvictExpiredConnectionsTask {

    @Autowired
    private PoolingHttpClientConnectionManager manager;

    @Scheduled(cron="0/5 * *  * * ? ")
    public void evictExpiredConnections(){
        //每隔5分钟清除失效链接
        manager.closeExpiredConnections();
    }
}

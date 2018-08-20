package com.winhxd.b2c.common.feign.pay;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.PAY_SERVICE, fallbackFactory = DownLoadStatementClientFallback.class)
public interface DownLoadStatementClient {

	@PostMapping(value = "/pay/6155/v1/downloadStatement", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseResult<String> downloadStatement(Date billDate);
	
	@PostMapping(value = "/pay/6156/v1/downloadFundFlow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseResult<String> downloadFundFlow(Date billDate);
	
	@PostMapping(value = "/pay/6157/v1/findDownloadRecord", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	ResponseResult<List<PayStatementDownloadRecord>> findDownloadRecord(@RequestBody PayStatementDownloadRecord record);
	
}

class DownLoadStatementClientFallback implements DownLoadStatementClient, FallbackFactory<DownLoadStatementClient>{
	private static final Logger logger = LoggerFactory.getLogger(DownLoadStatementClientFallback.class);
	
    private Throwable throwable;

    public DownLoadStatementClientFallback() {
    	
    }

    private DownLoadStatementClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }
	@Override
	public DownLoadStatementClient create(Throwable arg0) {
		return new DownLoadStatementClientFallback(throwable);
	}

	@Override
	public ResponseResult<String> downloadStatement(Date billDate) {
		logger.error("DownLoadStatementClient -> downloadStatement", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}
	
	@Override
	public ResponseResult<String> downloadFundFlow(Date billDate) {
		logger.error("DownLoadStatementClient -> downloadFundFlow", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	@Override
	public ResponseResult<List<PayStatementDownloadRecord>> findDownloadRecord(
			PayStatementDownloadRecord record) {
		logger.error("DownLoadStatementClient -> findDownloadRecord", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
	}

	
}
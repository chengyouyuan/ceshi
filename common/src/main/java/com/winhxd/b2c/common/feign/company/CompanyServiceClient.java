package com.winhxd.b2c.common.feign.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.Retail2cCompanyCondition;
import com.winhxd.b2c.common.domain.product.vo.CompanyInfo;

import feign.hystrix.FallbackFactory;

@FeignClient(value = ServiceName.USER_SERVICE, fallbackFactory = CompanyServiceFallback.class)
public interface CompanyServiceClient {

	@RequestMapping(value = "/company/v1/getCompanyInfoByPage/", method = RequestMethod.POST)
	ResponseResult<PagedList<CompanyInfo>> getCompanyInfoByPage(Retail2cCompanyCondition condition);
	
}

class CompanyServiceFallback implements CompanyServiceClient, FallbackFactory<CompanyServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceClient.class);
    private Throwable throwable;

    public CompanyServiceFallback() {
    	
    }

    private CompanyServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

	@Override
	public CompanyServiceClient create(Throwable arg0) {
		return new CompanyServiceFallback(throwable);
	}

	@Override
	public ResponseResult<PagedList<CompanyInfo>> getCompanyInfoByPage(Retail2cCompanyCondition condition) {
		logger.error("ProductServiceClient -> getCompanyInfoByPage", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}

}

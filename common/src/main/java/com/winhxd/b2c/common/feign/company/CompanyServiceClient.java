package com.winhxd.b2c.common.feign.company;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.Retail2cCompanyCondition;
import com.winhxd.b2c.common.domain.product.vo.CompanyInfo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = ServiceName.USER_SERVICE, fallbackFactory = CompanyServiceFallback.class)
public interface CompanyServiceClient {

	@RequestMapping(value = "/company/7001/v1/getCompanyInfoByPage", method = RequestMethod.POST)
	ResponseResult<PagedList<CompanyInfo>> getCompanyInfoByPage(Retail2cCompanyCondition condition);
	
	@RequestMapping(value = "/company/7002/v1/getCompanyInfoByCodes", method = RequestMethod.POST)
	ResponseResult<List<CompanyInfo>> getCompanyInfoByCodes(List<String> codes);
}
@Component
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
	
	@Override
	public ResponseResult<List<CompanyInfo>> getCompanyInfoByCodes(List<String> codes) {
		logger.error("ProductServiceClient -> getCompanyInfoByCodes", throwable);
		return new ResponseResult<>(BusinessCode.CODE_1001);
	}

}

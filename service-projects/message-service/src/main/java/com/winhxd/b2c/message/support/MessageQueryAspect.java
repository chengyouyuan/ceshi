package com.winhxd.b2c.message.support;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.message.enums.MsgCategoryEnum;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @ClassName: MessageQueryAspect
 * @Description: TODO
 * @Author Jesse Fan
 * @Date 2018-08-14 18:36
 **/
@Component
@Aspect
public class MessageQueryAspect {

	private static final Logger logger = LoggerFactory.getLogger(MessageQueryAspect.class);

	@AfterReturning(returning = "neteaseMsgVO", value = "@annotation(com.winhxd.b2c.message.support.annotation.MessageEnumConvertAnnotation)")
	public void orderEnumConvert(NeteaseMsgVO neteaseMsgVO) {
		logger.debug("对NeteaseMsgVO使用orderEnumConvert进行转换");
		if (null != neteaseMsgVO) {
			neteaseMsgVO.setMsgCategoryDesc(MsgCategoryEnum.getDescByCode(neteaseMsgVO.getMsgCategory()));
			neteaseMsgVO.setMsgCategorySummary(MsgCategoryEnum.getSummaryByCode(neteaseMsgVO.getMsgCategory()));
		}
	}

	@AfterReturning(returning = "pagedList", value = "@annotation(com.winhxd.b2c.message.support.annotation.MessageEnumConvertAnnotation)")
	public void orderEnumConvert(Object pagedList) {
		logger.debug("对NeteaseMsgVO Paged使用orderEnumConvert进行转换");
		if (pagedList instanceof PagedList) {
			List objList = ((PagedList) pagedList).getData();
			for (Iterator iterator = objList.iterator(); iterator.hasNext(); ) {
				Object object = iterator.next();
				if (object == null) {
					continue;
				}
				if(object instanceof NeteaseMsgVO){
					orderEnumConvert((NeteaseMsgVO) object);
				}
			}
		}
	}
}

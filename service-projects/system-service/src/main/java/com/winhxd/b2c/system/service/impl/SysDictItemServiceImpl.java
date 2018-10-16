package com.winhxd.b2c.system.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.SysConstant;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictItemCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.system.dao.SysDictItemMapper;
import com.winhxd.b2c.system.service.SysDictItemService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class SysDictItemServiceImpl implements SysDictItemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SysDictItemService.class);

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    @Override
    public Integer checkDictItem(SysDictItemCondition sysDictItemCondition) {
        AtomicReference<Integer> result = new AtomicReference<>(1);
        if (sysDictItemCondition == null) {
            LOGGER.info("参数为空");
            throw new BusinessException(BusinessCode.CODE_610030);
        }
        String value = sysDictItemCondition.getAppVersion();
        if (StringUtils.isEmpty(value)) {
            LOGGER.info("字典项的值为空");
            throw new BusinessException(BusinessCode.CODE_3040001);
        }
        List<SysDictItem> sysDictItems = sysDictItemMapper.selectByDictCode(SysConstant.APP_CHECK_VERSION);
        if (sysDictItems.size() == 0) {
            return 1;
        }
        sysDictItems.forEach(
                sysDictItem -> {
                    if (sysDictItem.getValue().equals(value)) {
                        result.set(0);
                    }
                });
        return result.get();
    }
}

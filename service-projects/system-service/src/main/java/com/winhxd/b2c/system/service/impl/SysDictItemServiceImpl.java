package com.winhxd.b2c.system.service.impl;

import com.winhxd.b2c.common.constant.SysConstant;
import com.winhxd.b2c.common.domain.system.dict.condition.AppVersionCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.system.dao.SysDictItemMapper;
import com.winhxd.b2c.system.service.SysDictItemService;
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
    public Integer checkDictItem(AppVersionCondition appVersionCondition) {
        AtomicReference<Integer> result = new AtomicReference<>(1);
        String value = appVersionCondition.getAppVersion();
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

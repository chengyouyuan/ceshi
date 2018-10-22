package com.winhxd.b2c.system.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;

import java.util.List;

/**
 * 字典数据管理服务
 * @author zhangzhengyang
 * @date 2018/9/26
 */
public interface SysDictService {

    /**
     * 新增字典组
     * @author zhangzhengyang
     * @date 2018/9/26
     * @param sysDict
     * @return int
     */
    int saveSysDict(SysDict sysDict);

    /**
     * 修改字典组
     * @author zhangzhengyang
     * @date 2018/9/26
     * @param sysDict
     * @return int
     */
    int modifySysDict(SysDict sysDict);

    /**
     * 查询字典组列表
     * @author zhangzhengyang
     * @date 2018/9/26
     * @param condition
     * @return
     */
    PagedList<SysDict> findSysDictPagedList(SysDictCondition condition);

    /**
     * 根据字典code查询字典数据
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param dictCode
     * @return
     */
    List<SysDictItem> findSysDictItemByDictCode(String dictCode);

    /**
     * 根据主键获取字典组信息
     * @author zhangzhengyang
     * @date 2018/9/26
     * @param id
     * @return
     */
    SysDict getSysDictById(Long id);

    /**
     * 根据主键删除
     * @author zhangzhengyang
     * @date 2018/9/26
     * @param id
     */
    int removeSysDictById(Long id);

}

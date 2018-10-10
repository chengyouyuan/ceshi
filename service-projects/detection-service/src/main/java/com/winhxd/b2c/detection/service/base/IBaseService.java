package com.winhxd.b2c.detection.service.base;

import java.util.List;

public interface IBaseService<T> {

	public T selectOne(T entity);

	public List<T> select(T entity);

	public List<T> selectAll();

	public int selectCount(T entity);

	public T selectByPrimaryKey(Object entity);

	public int insert(T entity);

	public int insertSelective(T entity);

	public int updateByPrimaryKey(T entity);

	public int updateByPrimaryKeySelective(T entity);

	public int delete(T entity);

	public int deleteByPrimaryKey(Object entity);

	public List<T> selectByExample(Object entity);

	public int selectCountByExample(Object entity);

    /**
     * 单表分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<T> selectPage(int pageNum, int pageSize, T entity);
}
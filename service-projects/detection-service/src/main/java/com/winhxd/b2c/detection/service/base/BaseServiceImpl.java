package com.winhxd.b2c.detection.service.base;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author louis
 * @Date 2016年1月22日 上午9:59:46
 */
@Service
public abstract class BaseServiceImpl<T> implements IBaseService<T> {

	@Autowired
    protected Mapper<T> mapper;

    public int insert(T entity){
        return mapper.insert(entity);
    }

    public int delete(T entity){
        return mapper.deleteByPrimaryKey(entity);
    }

    /**
     * 单表分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<T> selectPage(int pageNum,int pageSize,T arg0){
        PageHelper.startPage(pageNum, pageSize);
        //Spring4支持泛型注入
        if(arg0!=null){
        	return mapper.select(arg0);
        }else{
        	return mapper.select(null);
        }
    }

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectOne(java.lang.Object)
	 */
	@Override
	public T selectOne(T arg0) {
		// TODO Auto-generated method stub
		return mapper.selectOne(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#select(java.lang.Object)
	 */
	@Override
	public List<T> select(T arg0) {
		// TODO Auto-generated method stub
		return mapper.select(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectAll()
	 */
	@Override
	public List<T> selectAll() {
		// TODO Auto-generated method stub
		return mapper.selectAll();
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectCount(java.lang.Object)
	 */
	@Override
	public int selectCount(T arg0) {
		// TODO Auto-generated method stub
		return mapper.selectCount(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectByPrimaryKey(java.lang.Object)
	 */
	@Override
	public T selectByPrimaryKey(Object arg0) {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#insertSelective(java.lang.Object)
	 */
	@Override
	public int insertSelective(T arg0) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#updateByPrimaryKey(java.lang.Object)
	 */
	@Override
	public int updateByPrimaryKey(T arg0) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKey(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#updateByPrimaryKeySelective(java.lang.Object)
	 */
	@Override
	public int updateByPrimaryKeySelective(T arg0) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#deleteByPrimaryKey(java.lang.Object)
	 */
	@Override
	public int deleteByPrimaryKey(Object arg0) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectByExample(java.lang.Object)
	 */
	@Override
	public List<T> selectByExample(Object arg0) {
		// TODO Auto-generated method stub
		return mapper.selectByExample(arg0);
	}

	/* (non-Javadoc)
	 * @see com.lp.core.service.base.IBaseService#selectCountByExample(java.lang.Object)
	 */
	@Override
	public int selectCountByExample(Object arg0) {
		// TODO Auto-generated method stub
		return mapper.selectCountByExample(arg0);
	}


}
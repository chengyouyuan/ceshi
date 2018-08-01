package com.winhxd.b2c.common.domain.page;

public interface Page<T> {

	/**
	 * @author 魏冰
	 * @date 2017年4月6日 下午1:35:41
	 * @Description 总页数
	 * @version 1.0
	 * @return
	 */
	long getTotalPages();

	/**
	 * @author 魏冰
	 * @date 2017年4月6日 下午1:44:41
	 * @Description 页号
	 * @version 1.0
	 * @return
	 */
	long getPageNo();

	/**
	 * @author 魏冰
	 * @date 2017年4月6日 下午2:10:43
	 * @Description 上一页
	 * @version 1.0
	 * @return
	 */
	long getPreviousPageNo();

	/**
	 * @author 魏冰
	 * @date 2017年4月6日 下午2:10:43
	 * @Description 下一页
	 * @version 1.0
	 * @return
	 */
	long getNextPageNo();

}

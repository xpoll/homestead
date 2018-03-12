package cn.blmdz.home.dao;

import org.apache.ibatis.annotations.Param;

import cn.blmdz.home.model.UserThird;

public interface UserThirdDao extends BaseDao<UserThird> {

	/**
	 * 通过用户ID查找
	 * @param userId
	 * @return
	 */
	UserThird findByUserId(Long userId);
	/**
	 * 通过渠道用户名查找
	 * @param type
	 * @param username
	 * @return
	 */
	UserThird findByUsername(@Param("type") Integer type, @Param("username") String username);
}
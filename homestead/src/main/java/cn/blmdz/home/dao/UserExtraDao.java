package cn.blmdz.home.dao;

import cn.blmdz.home.model.UserExtra;

public interface UserExtraDao extends BaseDao<UserExtra> {

	/**
	 * 通过用户ID查找
	 * @param userId
	 * @return
	 */
	UserExtra findByUserId(Long userId);
}
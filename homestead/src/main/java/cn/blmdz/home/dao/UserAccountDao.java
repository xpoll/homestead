package cn.blmdz.home.dao;

import cn.blmdz.home.model.UserAccount;

public interface UserAccountDao extends BaseDao<UserAccount> {

	/**
	 * 通过用户ID查找
	 * @param userId
	 * @return
	 */
    UserAccount findByUserId(Long userId);
}
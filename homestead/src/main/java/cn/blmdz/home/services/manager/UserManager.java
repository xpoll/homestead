package cn.blmdz.home.services.manager;

import cn.blmdz.home.model.vo.SignVo;

public interface UserManager {

	/**
	 * 签到触发
	 */
	SignVo sign(Long userId);
}

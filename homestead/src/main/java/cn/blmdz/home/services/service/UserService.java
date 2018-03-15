package cn.blmdz.home.services.service;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.model.valid.LoginValid;
import cn.blmdz.home.model.valid.RegisterValid;
import cn.blmdz.home.model.valid.UserInfoValid;
import cn.blmdz.home.model.vo.UserInfoVo;

public interface UserService {
	
	/**
	 * 用户账密注册
	 */
	BaseUser registered(RegisterValid register);
	/**
	 * 账密登陆(社区手机号邮箱等需要验证密码)
	 */
	BaseUser login(LoginValid login);
	/**
	 * 第三方识别(登陆或注册)
	 */
	BaseUser recognition(RegisterValid register);
	
	/**
	 * 基本信息-用作展示
	 */
	BaseUser getUser(Long userId);
	
	/**
	 * 获取用户信息
	 * for
	 *  -- 用户中心-个人信息
	 */
	UserInfoVo findByid(Long id);
	
	/**
	 * 更新用户信息
	 */
	void updateUserInfo(Long id, UserInfoValid info);
	
	/**
	 * 登陆事件
	 * -- 登陆触发更新登陆时间
	 */
	void loginEvent(Long id);
}
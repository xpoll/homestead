package cn.blmdz.home.services.manager;

import cn.blmdz.home.model.third.ThirdUser;

/**
 * 外接第三方必须实现此接口
 * 
 * @author lm
 */
public interface ThirdManager {

	/**
	 * 获取用户基本信息（thirdUserId，nick，avatar）
	 * 
	 * @param authCode
	 * @param tuser
	 * @return
	 */
	ThirdUser getThirdUserId(String authCode, ThirdUser tuser);
	
	/**
	 * 用户开卡
	 * 
	 * @param requestId 请求号
	 * @param templateId 模板号
	 * @param authCode 授权号
	 */
	void card(String requestId, String templateId, String authCode);
	
	/**
	 * 领卡链接
     * @return
	 */
	String cardLink();
	
	String name();
}

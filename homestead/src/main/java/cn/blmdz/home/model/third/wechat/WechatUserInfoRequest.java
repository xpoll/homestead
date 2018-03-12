package cn.blmdz.home.model.third.wechat;

import java.util.HashMap;

public class WechatUserInfoRequest extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	
	public WechatUserInfoRequest (){}
	
	public WechatUserInfoRequest (String token, String openid) {
		this.put("access_token", token);
		this.put("lang", "zh_CN");
		this.put("openid", openid);
	}
}

package cn.blmdz.home.model.third.wechat;

import java.util.HashMap;

import cn.blmdz.home.properties.OtherProperties;

public class WechatUserTokenRequest extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	
	public WechatUserTokenRequest (){}
	
	public WechatUserTokenRequest (OtherProperties properties, String code) {
		this.put("appid", properties.getThird().getWechat().getAppId());
		this.put("secret", properties.getThird().getWechat().getAppSecret());
		this.put("grant_type", "authorization_code");
		this.put("code", code);
	}
}

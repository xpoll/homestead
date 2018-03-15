package cn.blmdz.home.model.third.wechat;

import java.util.HashMap;

public class WechatAppletSessionKeyRequest extends HashMap<String, String> {
	private static final long serialVersionUID = 1L;
	
	public WechatAppletSessionKeyRequest (){}
	
	public WechatAppletSessionKeyRequest (String appid, String secret, String code) {
        this.put("appid", appid);
        this.put("secret", secret);
        this.put("grant_type", "authorization_code");
        this.put("js_code", code);
    
	}
}

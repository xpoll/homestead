package cn.blmdz.home.model.third.wechat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WechatUserTokenResponse extends WechatBaseResponse {
	private String access_token;
	private Integer expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
}

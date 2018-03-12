package cn.blmdz.home.services.manager.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.kevinsawicki.http.HttpRequest;

import cn.blmdz.home.config.ThirdConfiguration;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.model.third.ThirdUser;
import cn.blmdz.home.model.third.wechat.WechatUserInfoRequest;
import cn.blmdz.home.model.third.wechat.WechatUserInfoResponse;
import cn.blmdz.home.model.third.wechat.WechatUserTokenRequest;
import cn.blmdz.home.model.third.wechat.WechatUserTokenResponse;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.services.manager.ThirdManager;
import cn.blmdz.home.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component("wechatThirdManager")
public class WechatThirdManager implements ThirdManager {
    
    @Autowired
    private OtherProperties properties;

	@Override
	public ThirdUser getThirdUserId(String authCode, ThirdUser tuser) {
		HttpRequest reqToken = HttpRequest.get(ThirdConfiguration.WECHAT_USER_TOKEN_URL, new WechatUserTokenRequest(properties, authCode), false);
		if (reqToken.ok()) {
			WechatUserTokenResponse tokenResponse = JsonMapper.nonEmptyMapper().fromJson(reqToken.body(), WechatUserTokenResponse.class);
			if (StringUtils.isBlank(tokenResponse.getErrcode())) {
				HttpRequest reqInfo = HttpRequest.get(ThirdConfiguration.WECHAT_USER_INFO_URL, new WechatUserInfoRequest(tokenResponse.getAccess_token(), tokenResponse.getOpenid()), false);
				WechatUserInfoResponse infoResponse = JsonMapper.nonEmptyMapper().fromJson(reqInfo.body(), WechatUserInfoResponse.class);
				if (reqInfo.ok() && StringUtils.isBlank(infoResponse.getErrcode())) {
				    tuser.setAvatar(infoResponse.getHeadimgurl());
				    tuser.setThirdUserId(infoResponse.getOpenid());
				    tuser.setNick(infoResponse.getNickname());
				    return tuser;
				}
			}
		}
        log.error("error get user.");

		return null;
	}

	@Override
	public void card(String requestId, String templateId, String authCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public String cardLink() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String name() {
        return ThirdChannel.WECHAT.toString();
    }

}

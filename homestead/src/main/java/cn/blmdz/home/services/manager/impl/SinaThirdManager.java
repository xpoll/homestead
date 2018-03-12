package cn.blmdz.home.services.manager.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;

import cn.blmdz.home.config.ThirdConfiguration;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.model.third.ThirdUser;
import cn.blmdz.home.model.third.sina.SinaUser;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.services.manager.ThirdManager;
import cn.blmdz.home.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("sinaThirdManager")
public class SinaThirdManager implements ThirdManager {
    
    @Autowired
    private OtherProperties properties;

	@Override
	@SuppressWarnings("unchecked")
	public ThirdUser getThirdUserId(String authCode, ThirdUser tuser) {
		Map<String, String> values = Maps.newHashMap();
		values.put("client_id", properties.getThird().getSina().getAppKey());
		values.put("client_secret", properties.getThird().getSina().getAppSecret());
		values.put("grant_type", "authorization_code");
		values.put("code", authCode);
		values.put("redirect_uri", properties.getThird().getSina().getRedirectUrl());

		HttpRequest reqToken = HttpRequest.post(ThirdConfiguration.SINA_USER_TOKEN_URL);
		reqToken.form(values);
		if (reqToken.ok()) {

			values.clear();
			values = JsonMapper.nonEmptyMapper().fromJson(reqToken.body(), Map.class);
			String access_token = values.get("access_token");
			String uid = values.get("uid");

			values = Maps.newHashMap();
			values.put("uid", uid);
			values.put("source", properties.getThird().getSina().getAppKey());
			values.put("access_token", access_token);

			HttpRequest reqInfo = HttpRequest.get(ThirdConfiguration.SINA_USER_INFO, values, false);
			if (reqInfo.ok()) {
				SinaUser suser = JsonMapper.nonEmptyMapper().fromJson(reqInfo.body(), SinaUser.class);
				suser.setProfile_url(ThirdConfiguration.SINA_PREFIX + suser.getProfile_url()); 
				tuser.setThirdUserId(String.valueOf(suser.getId()));
				tuser.setNick(suser.getScreen_name());
				tuser.setAvatar(suser.getAvatar_large());
				tuser.setUrl(suser.getProfile_url());
				return tuser;
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
        return ThirdChannel.SINA.toString();
    }

}

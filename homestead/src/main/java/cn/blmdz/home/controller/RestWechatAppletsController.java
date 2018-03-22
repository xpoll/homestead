package cn.blmdz.home.controller;

import static org.apache.commons.codec.binary.Base64.decodeBase64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.eventbus.EventBus;

import cn.blmdz.home.base.BaseUser;
import cn.blmdz.home.base.BaseVo;
import cn.blmdz.home.base.Response;
import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.event.LoginEvent;
import cn.blmdz.home.model.third.wechat.WechatAppletSessionKeyRequest;
import cn.blmdz.home.model.third.wechat.WechatAppletSessionKeyResponse;
import cn.blmdz.home.model.third.wechat.WechatAppletUserInfoResponse;
import cn.blmdz.home.model.valid.RegisterValid;
import cn.blmdz.home.services.manager.impl.SocketManagers;
import cn.blmdz.home.services.service.UserService;
import cn.blmdz.home.util.AESUtil;
import cn.blmdz.home.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信小程序用户识别
 * @author yongzongyang
 * @date 2018年3月19日
 */
@Slf4j
@RestController
@RequestMapping(value="/api/applet")
public class RestWechatAppletsController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private EventBus eventBus;
    

    /**
     * 解析识别记录用户
     * @param request
     * @param code
     * @param iv
     * @param encryptedData
     * @return
     */
    @RequestMapping(value="auth", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public Response<BaseUser> auth(HttpServletRequest request, @RequestParam("code") String code, @RequestParam("iv") String iv, @RequestParam("encryptedData") String encryptedData) {
        
        if (StringUtils.isBlank(code)) return Response.build(null);
        HttpRequest reqSession = HttpRequest.get("https://api.weixin.qq.com/sns/jscode2session", new WechatAppletSessionKeyRequest("wxe999d4c1904d3f23", "ddd36814cdd616810d390e4e5131260e", code), false);

        BaseUser user = null;
        if (reqSession.ok()) {
            String body = reqSession.body();
            log.debug("jscode2session info. info: {}", body);
            WechatAppletSessionKeyResponse sessionResponse = JsonMapper.nonEmptyMapper().fromJson(body, WechatAppletSessionKeyResponse.class);
            user = decryptToUser(sessionResponse.getSession_key(), iv, encryptedData);
            if (user == null) return Response.build(null);
            
            user.setSession(sessionResponse.getSession_key());
            request.getSession().setAttribute("user", user);
            SocketManagers.sessionKeys.put(user.getSession(), BaseVo.put(new Date(), user));
            log.info("welcome {}! user: {}", user.getNick(), user);
            eventBus.post(new LoginEvent(user.getId()));
        } else {
            log.error("jscode2session error.");
        }
        return Response.build(user);
    }
    
    /**
     * 封装识别
     * @param key
     * @param iv
     * @param encryptedData
     * @return
     */
    private BaseUser decryptToUser(String key, String iv, String encryptedData) {
        WechatAppletUserInfoResponse response = decrypt(key, iv, encryptedData);
        if (response == null) return null;
        RegisterValid register = new RegisterValid();
        register.setChannel(ThirdChannel.WECHAT);
        register.setNick(response.getNickName());
        register.setName(response.getOpenId());
        register.setGender(response.getGender());
        register.setAvatar(response.getAvatarUrl());
        return userService.recognition(register);
    }
    
    /**
     * 用户数据解密
     * @param key
     * @param iv
     * @param encryptedData
     * @return
     */
    private static WechatAppletUserInfoResponse decrypt(String key, String iv, String encryptedData) {
        try {
            String json = new String(AESUtil.instance.decrypt(decodeBase64(encryptedData), decodeBase64(key), decodeBase64(iv)), "UTF-8");
            if (StringUtils.isBlank(json)) return null;
            return JsonMapper.nonEmptyMapper().fromJson(json, WechatAppletUserInfoResponse.class);
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package cn.blmdz.home.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;

import cn.blmdz.home.enums.third.ThirdChannel;
import cn.blmdz.home.model.third.ThirdUser;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.sdk.ThirdUtil;
import cn.blmdz.home.services.manager.ThirdManager;
import cn.blmdz.home.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/third")
public class ThirdController {
	
	@Autowired
	@Qualifier("alipayThirdManager")
	private ThirdManager alipayThirdManager;

	@Autowired
	@Qualifier("sinaThirdManager")
	private ThirdManager sinaThirdManager;

	@Autowired
	@Qualifier("baiduThirdManager")
	private ThirdManager baiduThirdManager;

	@Autowired
	@Qualifier("wechatThirdManager")
	private ThirdManager wechatThirdManager;

    @Autowired
    private OtherProperties properties;
	
//	@Autowired
//	private ThirdUserService thirdUserService;

	/**
	 * 渠道切换
	 */
	private ThirdManager channel(ThirdChannel channel) {
		switch (channel) {
		case ALIPAY:
			return alipayThirdManager;
		case SINA:
		    return sinaThirdManager;
		case BAIDU:
		    return baiduThirdManager;
		case WECHAT:
		    return wechatThirdManager;
        default:
            return null;
		}
	}
	
	/**
	 * 阿里入口
	 */
	@RequestMapping(value="/alipay")
	public String alipay(HttpServletRequest request, HttpServletResponse response) {
		map(request);
		third(request, response, ThirdChannel.ALIPAY);
		return null;
	}

	/**
	 * 新浪入口
	 */
	@RequestMapping(value="/sina")
	public String sina(HttpServletRequest request, HttpServletResponse response) {
		map(request);
		third(request, response, ThirdChannel.SINA);
		return null;
	}
	
	/**
	 * 百度入口
	 */
	@RequestMapping(value="/baidu")
	public String baidu(HttpServletRequest request, HttpServletResponse response) {
		map(request);
		third(request, response, ThirdChannel.BAIDU);
		return null;
	}
	
	/**
	 * 微信入口
	 */
	@RequestMapping(value="/wechat")
	public String wechat(HttpServletRequest request, HttpServletResponse response) {
		map(request);
		third(request, response, ThirdChannel.WECHAT);
		return null;
	}

	/**
	 * 入口公共处理
	 */
	private void third(HttpServletRequest request, HttpServletResponse response, ThirdChannel channel) {
	    ThirdManager thirdManager = channel(channel);
		ThirdUser tuser = new ThirdUser(channel);
		tuser = ThirdUtil.getThirdUserId(request, response, tuser, thirdManager, false, properties);
		if (tuser == null) return ;
		
//		thirdUserService.recording(tuser);
		log.info("用户信息: {}", tuser);
		
		try {
			response.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打印入参
	 */
	private void map(HttpServletRequest request) {
		Map<String, String> requestMap = Maps.newHashMap();
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String param = enums.nextElement();
			requestMap.put(param, request.getParameter(param));
		}
		log.info("接收参数: {}", JsonMapper.nonDefaultMapper().toJson(requestMap));
	}


    /**
     * 领卡入口
     */
    @RequestMapping(value="/card")
    public void card(HttpServletRequest request, HttpServletResponse response) {
        map(request);
        ThirdManager thirdManager = channel(ThirdChannel.tran(request.getParameter("out_string")));
        thirdManager.card(request.getParameter("request_id"), request.getParameter("template_id"), request.getParameter("auth_code"));
        try {
            response.sendRedirect(thirdManager.cardLink());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 公共测试
     */
    @RequestMapping(value="/test")
    @ResponseBody
    public void test(HttpServletRequest request, HttpServletResponse response) {
        map(request);
    }
}

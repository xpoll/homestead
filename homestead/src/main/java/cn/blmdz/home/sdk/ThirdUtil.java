package cn.blmdz.home.sdk;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import cn.blmdz.home.config.ThirdConfiguration;
import cn.blmdz.home.model.third.ThirdUser;
import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.services.manager.ThirdManager;

/**
 * @author yongzongyang
 */
public class ThirdUtil {

	/**
	 * 获取第三方用户ID<br>
	 * 
	 * 调用前请校验信息<br>
	 * 调用后判断是否为空return进行sendRedirect<br>
	 * @param request
	 * @param response
	 * @param user 必要基础信息（third、business、type）
	 * @param thirdPartyManager 设置UserId和AccessToken
	 * @param session true 走session取，false 不走session取但设置session 未启用
	 * @return
	 */
	public static ThirdUser getThirdUserId(HttpServletRequest request, HttpServletResponse response, ThirdUser tuser, ThirdManager thirdManager, boolean session, OtherProperties properties) {
//		if (session) {
//			if (request.getSession().getAttribute(SDKUtil.THIRD_USER_ID) != null) {
//				return (ThirdUser) request.getSession().getAttribute(SDKUtil.THIRD_USER_ID);
//			}
//		}
		String authCode = null;
		switch (tuser.getThird()) {
		case ALIPAY:
			authCode = request.getParameter(ThirdConfiguration.ALIPAY_AUTH_CODE);
			break;
			
		case SINA:
			authCode = request.getParameter(ThirdConfiguration.SINA_AUTH_CODE);
			break;
			
		case BAIDU:
			authCode = request.getParameter(ThirdConfiguration.BAIDU_AUTH_CODE);
			break;
			
		case WECHAT:
			authCode = request.getParameter(ThirdConfiguration.WECHAT_AUTH_CODE);
			break;
			
		default:
			break;
		}
		if (StringUtils.isBlank(authCode)) {
			try {
				response.sendRedirect(ThirdUtil.getAuthCode(request.getRequestURL().toString(), tuser, properties));
				return null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * thirdPartyManager设置UserId
		 */
		return thirdManager.getThirdUserId(authCode, tuser);
	}
	
	/**
	 * 拼接地址
	 * @param url
	 * @param user
	 * @return
	 */
	private static String getAuthCode(String url, ThirdUser user, OtherProperties properties) {
		StringBuilder sb = new StringBuilder();
		switch (user.getThird()) {
		case ALIPAY:
			sb.append(ThirdConfiguration.ALIPAY_USER_AUTH_URL)
			.append("?")
			.append("app_id=")
			.append(properties.getThird().getAlipay().getAppId())
			.append("&scope=auth_base,auth_user");
			break;
			
		case SINA:
			sb.append(ThirdConfiguration.SINA_USER_AUTH_URL)
			.append("?")
			.append("client_id=")
			.append(properties.getThird().getSina().getAppKey())
			.append("&response_type=code");
			break;
			
		case BAIDU:
			sb.append(ThirdConfiguration.BAIDU_USER_AUTH_URL)
			.append("?")
			.append("client_id=")
			.append(properties.getThird().getBaidu().getAppKey())
			.append("&scope=basic&display=page&response_type=code");
			break;
			
		case WECHAT:
			sb.append(ThirdConfiguration.WECHAT_USER_AUTH_URL)
			.append("?")
			.append("appid=")
			.append(properties.getThird().getWechat().getAppId())
			.append("&response_type=code&scope=snsapi_userinfo");
			break;
			
		default:
			break;
		}
		sb
		.append("&")
		.append("redirect_uri=")
		.append(url);
		return sb.toString();
	}
}

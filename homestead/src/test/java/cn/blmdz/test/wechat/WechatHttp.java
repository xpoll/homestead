package cn.blmdz.test.wechat;

import java.util.Map;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.Maps;

public class WechatHttp {

	public static String appid = "";
	public static String secret = "";
	public static String access_token = "";
	
	
	public static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token";
	public static String ip_url = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
	public static String access_token_user_get_url = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static String access_token_user_refresh_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	public static String access_token_user_check_url = "https://api.weixin.qq.com/sns/auth";
	public static String user_info_url = "https://api.weixin.qq.com/sns/userinfo";

    public static String attention_url = "https://api.weixin.qq.com/cgi-bin/user/get"; 
    public static String attention_info_url = "https://api.weixin.qq.com/cgi-bin/user/info"; 
	

	public static void main(String[] args) {
		Map<String, String> params = Maps.newHashMap();
		
		// 获取应用授权access_token
		params.put("appid", appid);
		params.put("secret", secret);
		params.put("grant_type", "client_credential");
//		get(access_token_url, params);
        
        // 获取关注列表
        params.clear();
        params.put("access_token", access_token);
//        params.put("next_openid", "");
//        get(attention_url, params);
        
        // 获取关注用户信息
        params.clear();
        params.put("access_token", access_token);
        params.put("openid", "");
        params.put("lang", "zh_CN");
        get(attention_info_url, params);
		
		// 获取微信服务器IP
		params.clear();
		params.put("access_token", access_token);
//		get(ip_url, params);
		
		// 通过code换取网页授权access_token
		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=&redirect_uri=http://blmdz.cn/third/test&response_type=code&scope=snsapi_base&state=123#wechat_redirect
		// https://open.weixin.qq.com/connect/oauth2/authorize?appid=&redirect_uri=http://blmdz.cn/third/test&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect
		params.clear();
		params.put("appid", appid);
		params.put("secret", secret);
		params.put("grant_type", "authorization_code");
		params.put("code", "");
//		get(access_token_user_get_url, params);
		
		String user_access_token = "";
		String user_refresh_token = "";
		
		// 刷新access_token（如果需要）
		params.clear();
		params.put("appid", appid);
		params.put("grant_type", "refresh_token");
		params.put("refresh_token", user_refresh_token);
//		get(access_token_user_refresh_url, params);
		
		// 拉取用户信息(需scope为 snsapi_userinfo)
		params.clear();
		params.put("access_token", user_access_token);
		params.put("lang", "zh_CN");
		params.put("openid", "");
//		get(user_info_url, params);
		
		// 检验授权凭证（access_token）是否有效
		params.clear();
		params.put("access_token", user_access_token);
		params.put("openid", "");
//		get(access_token_user_check_url, params);
	}

	public static void get(String url, Map<String, String> params) {
		HttpRequest request = HttpRequest.get(url, params, false);
		System.out.println(request.ok());
		System.out.println(request.body());
	}
	/**
	 * post
	 */
	public static void post(String url, String msg) {
		HttpRequest request = HttpRequest.post(url);
		request.header(HttpRequest.HEADER_CONTENT_TYPE, HttpRequest.CONTENT_TYPE_JSON);
		request.send(msg);
		System.out.println(request.ok());
		System.out.println(request.body());
		
	}
}

package cn.blmdz.home.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.google.common.base.MoreObjects;

import cn.blmdz.home.properties.OtherProperties;
import cn.blmdz.home.sdk.AlipaySDK;
import cn.blmdz.home.util.JsonMapper;

@Configuration
public class ThirdConfiguration {
    
    private OtherProperties properties;
    
    @Autowired
    public ThirdConfiguration(OtherProperties properties) {
        properties.getThird().getBaidu().setRedirectUrl(properties.getThird().getDomain() + MoreObjects.firstNonNull(properties.getThird().getBaidu().getRedirectUrl(), ""));
        properties.getThird().getSina().setRedirectUrl(properties.getThird().getDomain() + MoreObjects.firstNonNull(properties.getThird().getSina().getRedirectUrl(), ""));
        this.properties = properties;
        System.out.println(JsonMapper.nonDefaultMapper().toJson(properties));
    }

    public static final String ALIPAY_AUTH_CODE = "auth_code";
    public static final String ALIPAY_USER_AUTH_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
    public static final String ALIPAY_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
    
    public static final String BAIDU_AUTH_CODE = "code";
    public static final String BAIDU_USER_AUTH_URL = "http://openapi.baidu.com/oauth/2.0/authorize";
    public static final String BAIDU_USER_TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";
    public static final String BAIDU_USER_INFO = "https://openapi.baidu.com/rest/2.0/passport/users/getLoggedInUser";
    public static final String BAIDU_AVATAR_PREFIX = "http://himg.bdimg.com/sys/portrait/item/";
    public static final String BAIDU_AVATAR_SUFFIX = ".jpg";

    public static final String SINA_AUTH_CODE = "code";
    public static final String SINA_USER_AUTH_URL = "https://api.weibo.com/oauth2/authorize";
    public static final String SINA_USER_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
    public static final String SINA_USER_INFO = "https://api.weibo.com/2/users/show.json";
    public static final String SINA_PREFIX = "http://weibo.com/";

    public static final String WECHAT_AUTH_CODE = "code";
    public static final String WECHAT_USER_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    public static final String WECHAT_USER_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WECHAT_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	public static final String WECHAT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	public static final String WECHAT_IP_URL = "https://api.weixin.qq.com/cgi-bin/getcallbackip";
	public static final String WECHAT_ACCESS_TOKEN_USER_REFRESH_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	public static final String WECHAT_ACCESS_TOKEN_USER_CHECK_URL = "https://api.weixin.qq.com/sns/auth";

    @Bean
    public AlipaySDK getInstance() {
        AlipayClient alipayClient = new DefaultAlipayClient(
                ALIPAY_GATEWAY_URL,
                properties.getThird().getAlipay().getAppId(),
                properties.getThird().getAlipay().getPrivateKey(),
                AlipayConstants.FORMAT_JSON,
                AlipayConstants.CHARSET_UTF8,
                properties.getThird().getAlipay().getPublicKey(),
                AlipayConstants.SIGN_TYPE_RSA2);
        return new AlipaySDK(alipayClient);
    }
}

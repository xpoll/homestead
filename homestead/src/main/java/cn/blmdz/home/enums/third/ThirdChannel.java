package cn.blmdz.home.enums.third;

/**
 * 第三方渠道
 * 
 * @author yongzongyang
 */
public enum ThirdChannel {
	COMMUNITY("community", "社区账号", 1),
	MOBILE("mobile", "手机号", 2),
	EMAIL("email", "邮箱", 3),
	ALIPAY("alipay", "支付宝", 4),
	SINA("sina", "新浪", 5),
	BAIDU("baidu", "百度", 6),
	WECHAT("wechat", "微信", 7),
	QQ("qq", "QQ", 8),
	;
	
	String code;
	String desc;
	Integer value;
	
	private ThirdChannel(String code, String desc, Integer value) {
		this.code = code;
		this.desc = desc;
		this.value = value;
	}
	
	public String code() {
		return this.code;
	}
	
	public Integer value() {
		return this.value;
	}

	public static ThirdChannel tran(String code) {
		for (ThirdChannel item : ThirdChannel.values()) {
			if (item.code.equals(code)) return item;
		}
		return null;
	}
}

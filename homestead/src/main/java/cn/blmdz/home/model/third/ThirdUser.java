package cn.blmdz.home.model.third;

import cn.blmdz.home.enums.third.ThirdChannel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 第三方用户信息
 * 
 * @author yongzongyang
 * @date 2017年6月9日
 */
@Data
@NoArgsConstructor
public class ThirdUser {
	/**
	 * 系统内主键ID
	 */
	private Long id;
	/**
	 * 类型
	 */
	private ThirdChannel third;
	/**
	 * 第三方用户ID
	 */
	private String thirdUserId;
	/**
	 * 昵称
	 */
	private String nick;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * 地址
	 */
	private String url;
	
	public ThirdUser(ThirdChannel third) {
		this.third = third;
	}
}

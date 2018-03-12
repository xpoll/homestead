package cn.blmdz.home.model.third.sina;

import lombok.Data;

/**
 * 新返回浪用户信息
 * @author lm
 */
@Data
public class SinaUser {
	private Long id;
	private String screen_name;
	private String avatar_large;
	private String profile_url;
}

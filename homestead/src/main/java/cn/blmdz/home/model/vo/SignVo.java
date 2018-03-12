package cn.blmdz.home.model.vo;

import lombok.Data;

/**
 * 签到返回
 */
@Data
public class SignVo {

	private Integer addActiveDay;// 增加活跃天数
	private Integer addGold;// 增加金币
	private Integer level;// 当前级别
	private Integer activeDay; // 当前活跃天
	private Boolean upgrade;// 是否升级
}

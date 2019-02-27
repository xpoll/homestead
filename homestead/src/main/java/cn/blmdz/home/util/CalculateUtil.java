package cn.blmdz.home.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import cn.blmdz.home.base.Constants;
import cn.blmdz.home.enums.third.ThirdChannel;

public class CalculateUtil {
	
	/**
	 * 是否可以签到
	 */
	public static boolean sign(Date old) {
		if (old == null) return true;
    	Calendar calendar = Calendar.getInstance();
    	String o = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    	calendar.setTime(old);
		return !o.equals(String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
	}

    /**
     * 是否是连续签到
     */
	public static boolean signContinue(Date old) {
        if (old == null) return false;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String o = String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        calendar.setTime(old);
        return o.equals(String.valueOf(calendar.get(Calendar.YEAR)) + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
	}
    
    /**
     * 账号渠道
     */
    public static ThirdChannel channel(String username) {
        if (StringUtils.isBlank(username)) return null;
        if (username.trim().matches(Constants.MOBILE)) return ThirdChannel.MOBILE;
        if (username.trim().matches(Constants.EMAIL)) return ThirdChannel.EMAIL;
        if (username.trim().matches(Constants.COMMUNITY)) return ThirdChannel.COMMUNITY;
        return null;
    }

    /**
     * 根据活跃天返回对应等级
     *  top 30
     */
    public static Integer level(Integer activeDay) {
        activeDay = activeDay/10;
        if (activeDay <= 0) return 0;
        else if (activeDay == 1) return 1;
        else if (activeDay >= 786) return 30;
        else return (int) (Math.sqrt(activeDay-2) + 2);
    }
    /**
     * 根据等级返回对应10倍活跃天
     *  top 30
     */
    public static Integer activeDay(Integer level) {
        if (level <= 0) return 0;
        else if (level == 1) return 10;
        else if (level >= 30) return 7860;
        else return ((level - 2) * (level - 2) + 2) * 10;
    }
    
    /**
     * 获取星座
     */
    public static String constellation(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int i = Integer.parseInt(String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    	if (i >= 120 && i <= 218) return "水瓶座";
    	if (i >= 219 && i <= 320) return "双鱼座";
    	if (i >= 321 && i <= 419) return "白羊座";
    	if (i >= 420 && i <= 520) return "金牛座";
    	if (i >= 521 && i <= 621) return "双子座";
    	if (i >= 622 && i <= 722) return "巨蟹座";
    	if (i >= 723 && i <= 822) return "狮子座";
    	if (i >= 823 && i <= 922) return "处女座";
    	if (i >= 923 && i <= 1023) return "天秤座";
    	if (i >= 1024 && i <= 1122) return "天蝎座";
    	if (i >= 1123 && i <= 1221) return "射手座";
    	return "魔羯座";
    }

    /**
     * 获取城市
     */
    public static List<String> city() {
        List<String> citys = Lists.newArrayList();
        citys.add("北京");// 京
        citys.add("天津");// 津
        citys.add("河北");// 冀
        citys.add("山西");// 晋
        citys.add("内蒙古");// 蒙
        citys.add("辽宁");// 辽
        citys.add("吉林");// 吉
        citys.add("黑龙江");// 黑
        citys.add("上海");// 沪
        citys.add("江苏");// 苏
        citys.add("浙江");// 浙
        citys.add("安徽");// 皖
        citys.add("福建");// 闽
        citys.add("江西");// 赣
        citys.add("山东");// 鲁
        citys.add("河南");// 豫
        citys.add("湖北");// 鄂
        citys.add("湖南");// 湘
        citys.add("广东");// 粤
        citys.add("广西");// 桂
        citys.add("海南");// 琼
        citys.add("四川");// 川
        citys.add("贵州");// 黔
        citys.add("云南");// 滇
        citys.add("重庆");// 渝
        citys.add("西藏");// 藏
        citys.add("陕西");// 陕
        citys.add("甘肃");// 甘
        citys.add("青海");// 青
        citys.add("宁夏");// 宁
        citys.add("新疆");// 新
        citys.add("香港");// 港
        citys.add("澳门");// 澳
        citys.add("台湾");// 台
        return citys;
    }
}

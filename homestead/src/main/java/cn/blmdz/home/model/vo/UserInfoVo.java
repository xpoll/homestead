package cn.blmdz.home.model.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.blmdz.home.base.Constants;
import cn.blmdz.home.enums.EnumsGender;
import lombok.Data;

@Data
public class UserInfoVo {
    private String nick;//昵称
    private String avatar;//头像
    private Integer level;//等级(基于活跃天)
    private Integer activeDay;//活跃天(10倍)
    private Integer backActiveDay;//上一级活跃天(10倍)
    private Integer nextActiveDay;//下一级活跃天(10倍)
    private Integer gender;//性别(0未知1男2女)
    @JsonFormat(pattern = Constants.Y, timezone = Constants.TIMEZONE)
    private Date signDate;//最后签到日期
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date loginTime;//最后登陆时间
    private String school;//学校
    @JsonFormat(pattern = Constants.Y, timezone = Constants.TIMEZONE)
    private Date birthday;//生日
    private String city;//城市
    private String constellation;//星座(基于生日)
    private String description;//个人简介
    
    public String getGenderShow() {
        return EnumsGender.tran(this.gender).desc();
    }
}

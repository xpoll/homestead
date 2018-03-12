package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * 用户
 */
@Data
public class User {
    private Long id;//ID
    private String nick;//昵称
    private String avatar;//头像
    private Integer vip;//VIP等级
    private Integer level;//等级(基于活跃天)
    private Integer activeDay;//活跃天(10倍)
    private Integer gender;//性别(0未知1男2女)
    private Date signDate;//最后签到日期
    private Date loginTime;//最后登陆时间
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}

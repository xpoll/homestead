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
public class UserThird {
    private Long id;//ID
    private Long userId;//用户ID
    private Integer channelId;//渠道ID 枚举
    private String username;//账号
    private String password;//密码
    private String extraJson;//其他json数据
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}

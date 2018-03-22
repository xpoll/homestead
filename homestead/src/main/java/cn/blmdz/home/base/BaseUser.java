package cn.blmdz.home.base;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class BaseUser {
    private Long id;//ID
    private String nick;//昵称
    private String avatar;//头像
    private Integer level;//等级(基于活跃天)
    @JsonFormat(pattern = Constants.Y, timezone = Constants.TIMEZONE)
    private Date signDate;//最后签到日期
    private String session;//用作session
}

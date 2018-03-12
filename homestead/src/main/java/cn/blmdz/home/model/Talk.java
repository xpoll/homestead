package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * TALK
 */
@Data
public class Talk {
    private Long id;//ID
    private Long userId;//用户ID
    private Boolean original;//原创(0转发1原创)
    private Long talkId;//原创TALKID(原创为-1)
    private Integer privacy;//私密(0公开1好友可见2自己可见) EnumsPrivacy
    private String content;//TALK内容
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}

package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * TALK评论表
 */
@Data
public class TalkReply {
    private Long id;//ID
    private Long userId;//用户ID
    private Long talkId;//TALKID
    private Long talkOwnerId;//TALK所有者ID(冗余)
    // 主回复ID 和 回复至的用户ID 并存用于定位
    private Long superId;//主回复ID
    private Long replyToId;//回复至的用户ID
    private String content;//评论/回复内容
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}

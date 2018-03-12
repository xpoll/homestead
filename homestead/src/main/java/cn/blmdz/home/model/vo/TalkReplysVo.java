package cn.blmdz.home.model.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * TALK评论表
 */
@Data
public class TalkReplysVo {
    private Long id;//ID
    private Long userId;//用户ID
    private String nick;//用户昵称
    private Long replyToId;//回复至的用户ID
    private String replayToNick;//回复至的用户昵称
    private String content;//评论/回复内容
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
}

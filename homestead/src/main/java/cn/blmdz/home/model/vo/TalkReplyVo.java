package cn.blmdz.home.model.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * TALK评论表
 */
@Data
public class TalkReplyVo {
    private Long id;//ID
    private Long userId;//用户ID
    private String nick;//用户昵称
    private String content;//评论/回复内容
    
    private List<TalkReplysVo> replys;

    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
}

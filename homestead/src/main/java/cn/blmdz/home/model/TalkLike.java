package cn.blmdz.home.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.base.Constants;
import lombok.Data;

/**
 * TALK赞表
 */
@Data
public class TalkLike {
    private Long id;//ID
    private Long userId;//用户ID
    private Long talkId;//说说ID
    @JsonIgnore
    private Boolean status;//状态(0不可用1可用)
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date updateTime;//更新时间
}

package cn.blmdz.home.model.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.blmdz.home.base.Constants;
import cn.blmdz.home.enums.EnumsPrivacy;
import lombok.Data;

@Data
public class TalkVo {
    
    private Long id;//ID
    private String nick;//用户昵称
    private String content;//TALK内容
    private Integer privacy;//私密(0公开1好友可见2自己可见) EnumsPrivacy
    private Boolean original;//原创(0转发1原创)
    private TalkForwardVo talkForwardVo;//转发原创信息
    private Integer forward;//转发
    private Integer reply;//评论
    private Integer like;//赞
    private List<TalkReplyVo> replys;// 回复和评论
    
    @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
    private Date createTime;//创建时间
    
    public String getPrivacyShow() {
        return EnumsPrivacy.tran(this.privacy).desc();
    }

    @Data
    public static class TalkForwardVo {
        private Long id;//ID
        private Long userId;//用户ID
        private String nick;//用户昵称
        private String content;//TALK内容
        @JsonFormat(pattern = Constants.YH, timezone = Constants.TIMEZONE)
        private Date createTime;//创建时间
    }
}

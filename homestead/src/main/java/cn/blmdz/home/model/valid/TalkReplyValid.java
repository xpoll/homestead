package cn.blmdz.home.model.valid;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class TalkReplyValid {
    @NotNull(message = "请求数据错误")
    private Long talkId;
    @Length(max = 500, message = "最大字数为500")
    @NotBlank(message = "内容不能为空")
    private String content;

    private Long superId;//主回复ID
    private Long replyToId;//回复至的用户ID
}

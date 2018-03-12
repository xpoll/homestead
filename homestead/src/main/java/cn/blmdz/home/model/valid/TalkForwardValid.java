package cn.blmdz.home.model.valid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TalkForwardValid {
    @NotNull(message = "请求数据错误")
    private Long talkId;//转发的TALKID
    @NotNull(message = "请选择私密")
    @Min(value = 0, message = "请求数据错误")
    @Max(value = 2, message = "请求数据错误")
    private Integer privacy;//私密(0公开1好友可见2自己可见) EnumsPrivacy
    @Length(max = 500, message = "最大字数为500")
    private String content = "转发";//TALK内容
    @JsonIgnore
    private Boolean original = Boolean.FALSE.booleanValue();//原创(0转发1原创)
}

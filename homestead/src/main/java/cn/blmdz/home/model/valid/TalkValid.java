package cn.blmdz.home.model.valid;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class TalkValid {
    @NotNull(message = "请选择私密")
    @Min(value = 0, message = "请求数据错误")
    @Max(value = 2, message = "请求数据错误")
    private Integer privacy;//私密(0公开1好友可见2自己可见) EnumsPrivacy
    @Length(max = 500, message = "最大字数为500")
    @NotBlank(message = "内容不能为空")
    private String content;//TALK内容
    @JsonIgnore
    private Boolean original = Boolean.TRUE.booleanValue();//原创(0转发1原创)
}

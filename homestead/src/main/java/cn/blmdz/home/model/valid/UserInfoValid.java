package cn.blmdz.home.model.valid;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import cn.blmdz.home.base.Constants;
import lombok.Data;

@Data
public class UserInfoValid {
    @NotBlank(message = "昵称不能为空")
    @Length(max = 20, message = "昵称最大字数为20")
    private String nick;//昵称
    @NotNull(message = "请选择性别")
    @Min(value = 0, message = "请求数据错误")
    @Max(value = 2, message = "请求数据错误")
    private Integer gender;//性别(0未知1男2女)
    @Length(max = 50, message = "学校最大字数为50")
    private String school;//学校
    @DateTimeFormat(pattern = Constants.Y)
    private Date birthday;//生日
    @NotBlank(message = "请选择城市")
    private String city;//城市
    private String description;//个人简介
}

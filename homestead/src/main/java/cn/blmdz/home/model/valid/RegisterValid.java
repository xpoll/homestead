package cn.blmdz.home.model.valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.blmdz.home.enums.third.ThirdChannel;
import lombok.Data;

@Data
public class RegisterValid {

    @NotBlank(message = "账号不能为空")
    @Length(min = 6, max = 20, message = "账号6-20")
	private String name;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码6-20")
	private String pwd;
    @NotBlank(message = "验证码不能为空")
    @Length(min = 4, max = 4, message = "验证码4")
    private String code;
    @JsonIgnore
    private ThirdChannel channel;

    private String nick;//昵称
    private String avatar;//头像
    private Integer gender;//性别(0未知1男2女)
}

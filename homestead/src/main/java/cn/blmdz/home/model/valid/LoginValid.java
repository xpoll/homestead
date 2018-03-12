package cn.blmdz.home.model.valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import cn.blmdz.home.enums.third.ThirdChannel;
import lombok.Data;

@Data
public class LoginValid {

    @NotBlank(message = "账号不能为空")
    @Length(min = 6, max = 20, message = "账号6-20")
	private String name;
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码6-20")
	private String pwd;
    
    private ThirdChannel channel;

}

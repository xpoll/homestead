package cn.blmdz.home.baiduyun;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.alibaba.fastjson.JSON;

import lombok.Data;

@Data
public class BaiduyunRequestVo {
	private String id;
    @NotBlank(message = "key不能为空")
	private String key;
	private String pwd;
    @NotNull(message = "类型不能为空")
	private Boolean encryption;
    
    public static void main(String[] args) {
    	BaiduyunRequestVo vo = new BaiduyunRequestVo();
    	vo.setKey("adf");
    	vo.setPwd("fff");
    	vo.setEncryption(true);
    	System.out.println(JSON.toJSONString(vo));
	}
}

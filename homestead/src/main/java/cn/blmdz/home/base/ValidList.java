package cn.blmdz.home.base;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class ValidList<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Valid
	@NotEmpty(message = "请求数据不能为空")
	private List<E> listNotNull;
	
	@Valid
	private List<E> listCanNull;

	
}
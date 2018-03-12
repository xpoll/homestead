package cn.blmdz.home.base;

import java.io.Serializable;

import cn.blmdz.home.enums.EnumsError;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 返回数据定义封装
 * @author lm
 * @date 2016年12月7日 下午11:46:42
 * @param <T>
 */
@Data
public class Response<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Setter(AccessLevel.NONE)
	private String message;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private Boolean success;
	
	@Setter(AccessLevel.NONE)
	private T data;

	public static Response<Boolean> ok() {
		return build(Boolean.TRUE);
	}
	
	public Response<T> buildEnum(EnumsError enumsError) {
		this.message = enumsError.desc();
		return this;
	}
	
	public Response<T> message(String msg) {
		this.message = msg;
		return this;
	}
	
	public Response<T> orNull(EnumsError enumsError) {
		if (this.getData() != null)
			return this;
		this.message = enumsError.desc();
		return this;
	}

	public Boolean isSuccess() {
		return success;
	}
	
	public static<T> Response<T> build(T data) {
		Response<T> response = new Response<T>();
		if (data == null) {
			response.message = EnumsError.ERROR_999998.desc();
			response.success = Boolean.FALSE.booleanValue();
			return response;
		}
		response.message = EnumsError.ERROR_000000.desc();
		response.success = Boolean.TRUE.booleanValue();
		response.data = data;
		return response;
	}
}

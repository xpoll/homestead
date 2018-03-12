package cn.blmdz.home.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

import cn.blmdz.home.base.Response;
import cn.blmdz.home.enums.EnumsError;
import cn.blmdz.home.exception.WebJspException;
import lombok.extern.slf4j.Slf4j;

/**
 * 最上级全局异常封装处理
 * @author yangyz
 * @date 2016年12月2日下午5:17:35
 */
@Slf4j
@ControllerAdvice
@Configuration
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
    public Response<?> exceptionHandler(
    		Exception e) throws Exception {
		
		if (e instanceof WebJspException) {
			
			log.debug("WebJspException: {}", e.getMessage());
			return Response.build(null).message(e.getMessage());
		} else if (e instanceof MethodArgumentNotValidException) {
			
			MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
			log.debug("MethodArgumentNotValidException: {}", ex.getBindingResult().getFieldError().getDefaultMessage());
			return Response.build(null).message(ex.getBindingResult().getFieldError().getDefaultMessage());
		} else if (e instanceof MultipartException) {
		    
            log.debug("MultipartException: {}",  e.getMessage());
            return Response.build(null).message(e.getMessage());
		}
		
		log.debug("Exception");
		e.printStackTrace();
		return Response.build(null).orNull(EnumsError.ERROR_999999);
    }
}

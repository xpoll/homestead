package cn.blmdz.home.exception;

import cn.blmdz.home.enums.EnumsError;

/**
 * extends RuntimeException
 * 项目异常返回
 * @author yangyz
 * @date 2016年12月2日下午5:07:58
 */
public class WebJspException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

    public WebJspException() {
        super();
    }

    public WebJspException(String message) {
        super(message);
    }
    
    public WebJspException(EnumsError enumsError) {
        super(enumsError.desc());
    }

    public WebJspException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WebJspException(Throwable throwable) {
        super(throwable);
    }

}

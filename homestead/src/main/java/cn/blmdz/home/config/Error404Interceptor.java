package cn.blmdz.home.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.blmdz.home.base.Constants;

/**
 * 跨域
 * 处理 FileNotFoundException
 * @author yangyz
 */
public class Error404Interceptor implements HandlerInterceptor {
    
    private Boolean debugger;
    
    public Error404Interceptor(Boolean debugger) {
        this.debugger = debugger;
    }

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
        if (debugger) {
            //response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8088"); // cookie信息携带必须设置来源
            response.setHeader("Access-Control-Allow-Credentials", "true"); // 携带cookie信息
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        }
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (response.getStatus() == 404 && modelAndView != null)
			modelAndView.setViewName(Constants.PAGE_NOT_FOUND);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

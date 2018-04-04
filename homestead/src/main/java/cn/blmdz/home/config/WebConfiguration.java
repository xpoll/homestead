package cn.blmdz.home.config;

import java.util.Properties;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.pagehelper.PageHelper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import cn.blmdz.aide.file.FileServer;
import cn.blmdz.aide.file.qiniu.QiniuFileServer;
import cn.blmdz.home.properties.OtherProperties;

/**
 * 额外总配置文件
 * @author yangyz
 * @date 2016年12月2日下午5:22:26
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {
    
	@Autowired
    private OtherProperties properties;

	/**
	 * guava异步通知
	 */
	@Bean
	public AsyncEventBus asyncEventBus() {
		return new AsyncEventBus(Executors.newScheduledThreadPool(5));
	}
	
	/**
	 * guava通知
	 */
	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}

    /**
     * 图片服务
     * @return
     */
    @Bean
    public FileServer fileServer() {
        
        return new QiniuFileServer(
                properties.getOos().getEndpoint(),
                properties.getOos().getAccessKey(),
                properties.getOos().getAccessSecret(),
                properties.getOos().getBucketName());

//        return new AliyunImageServer(
//                properties.getOos().getEndpoint(),
//                properties.getOos().getAccessKey(),
//                properties.getOos().getAccessSecret(),
//                properties.getOos().getBucketName());
    }
    
	/**
	 * 分页插件
	 */
	@Bean
    public PageHelper pageHelper(DataSource dataSource) {
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        pageHelper.setProperties(properties);
        return pageHelper;
    }
	
	/**
	 * 调试log
	 */
	@Bean
	public PageErrorFilter filter() {
		return new PageErrorFilter(properties.getDevMode());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry){
	    // 增加处理FileNotFound
        registry.addInterceptor(new Error404Interceptor(false));
	}

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 用于html的后缀匹配
        configurer.setUseSuffixPatternMatch(false);
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 跨域访问
        registry.addMapping("/**")
                .allowedHeaders(CorsConfiguration.ALL)
                .allowedOrigins(CorsConfiguration.ALL)
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE")
                .allowCredentials(true)
                .maxAge(3600);
        
        /**
         * registry.addMapping("/**").maxAge(3600);
         * 
         * 或者在Interceptor或过滤器中增加以下配置
         * 
         * response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8088"); // cookie信息携带必须设置来源
         * response.setHeader("Access-Control-Allow-Credentials", "true"); // 携带cookie信息
         * response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
         * response.setHeader("Access-Control-Max-Age", "3600");
         * response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
         */
    }
	
}

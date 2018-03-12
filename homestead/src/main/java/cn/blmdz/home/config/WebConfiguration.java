package cn.blmdz.home.config;

import java.util.Properties;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.github.pagehelper.PageHelper;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import cn.blmdz.aide.file.ImageServer;
import cn.blmdz.aide.file.aliyun.AliyunImageServer;
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

	@Bean
	public AsyncEventBus asyncEventBus() {
		return new AsyncEventBus(Executors.newScheduledThreadPool(5));
	}
	
	@Bean
	public EventBus eventBus() {
		return new EventBus();
	}

    /**
     * 图片服务
     * @return
     */
    @Bean
    public ImageServer imageServer() {

        return new AliyunImageServer(
                properties.getOos().getEndpoint(),
                properties.getOos().getAccessKey(),
                properties.getOos().getAccessSecret(),
                properties.getOos().getBucketName());
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
		return new PageErrorFilter(true);
//		return new PageErrorFilter(properties.getDevMode());
	}

	/**
	 * 跨域 增加处理FileNotFound
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new Error404Interceptor(properties.getDevMode()));
	}
	
}

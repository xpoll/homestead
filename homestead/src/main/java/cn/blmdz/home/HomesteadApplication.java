package cn.blmdz.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.blmdz.home.properties.OtherProperties;

@SpringBootApplication
@MapperScan("cn.blmdz.home.dao")
@EnableConfigurationProperties(OtherProperties.class)
public class HomesteadApplication extends WebMvcConfigurerAdapter {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
    
	public static void main(String[] args) {
	    SpringApplication.run(HomesteadApplication.class, args);
	}
}

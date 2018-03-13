package cn.blmdz.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import cn.blmdz.home.properties.OtherProperties;

@SpringBootApplication
@MapperScan("cn.blmdz.home.dao")
@EnableConfigurationProperties(OtherProperties.class)
public class HomesteadApplication {
    
	public static void main(String[] args) {
	    SpringApplication.run(HomesteadApplication.class, args);
	}
}
